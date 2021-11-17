package com.headout.vendor.plugins.utils

import com.headout.vendor.api.Result
import com.headout.vendor.api.exceptions.VendorInventoryException
import retrofit2.HttpException
import rx.Observable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import retrofit2.adapter.rxjava.Result as RetrofitResult

/**
 * Created by Mohit on 14/11/18.
 */

fun ClosedRange<Int>.random() =
		Random().nextInt(endInclusive - start) + start

fun computeNextTime(count: Int): Long {
	return (2.0.pow(count) * 1000 + (0..1000).random()).toLong()
}

fun isRateLimitError(error: Throwable, checkErrorCode:Int): Boolean {
	return (error is HttpException) && error.code() == checkErrorCode
}

fun <T : Throwable> exponentialBackOff(maxRetries: Int,
									   errors: Observable<T>,
									   rateLimitErrorCode:Int,
									   errorCodeChecker: (Throwable, Int) -> Boolean): Observable<out Any> {
	return errors.zipWith(Observable.range(0, maxRetries + 1)) { error, i -> if (errorCodeChecker(error, rateLimitErrorCode)) Pair(error, i) else Pair(error, maxRetries + 1) }
			.flatMap {(error, retries) ->
				if (retries >= maxRetries) {
					Observable.error(error)
				} else {
					Observable.timer(computeNextTime(retries), TimeUnit.MILLISECONDS)
				}
			}
}


fun <T, R> RetrofitResult<T>.runIfSuccessful(block: (T) -> R) : R {
	if(!isError) {
		val response = response()!!
		if(response.isSuccessful) {
			val res = response.body()!!
			return block(res)
		} else throw VendorInventoryException("Response Code: ${response.code()} Body: ${response.body()}")
	} else throw VendorInventoryException("Error from Vendor", error()!!)
}

inline fun <T : Any> wrapError(body: () -> T): Result<T> {
	return try {
		Result.success(body.invoke())
	} catch (ex: Exception) {
		Result.error(ex.toVendorPluginException())
	}
}
