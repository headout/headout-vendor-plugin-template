package com.headout.vendor.plugins.utils

import com.google.common.base.CaseFormat
import com.headout.vendor.api.IHeadoutInventoryApi
import com.headout.vendor.api.Result
import com.headout.vendor.api.enums.InventoryAvailability
import com.headout.vendor.api.exceptions.IIgnoreException
import com.headout.vendor.api.exceptions.InventoryUnavailableException
import com.headout.vendor.api.exceptions.VendorPluginException
import com.headout.vendor.api.models.IInventoryThreshold
import com.headout.vendor.api.models.InventoryRangeQuery
import com.headout.vendor.api.models.InventoryRangeResponse
import com.headout.vendor.api.models.InventorySlotInfo
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.HttpException
import tourlandish.common.enums.slack.SlackChannel

/**
 * Created by madki on 19/05/18.
 */
val timeStampFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
val timeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss")
val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
val tourCMSTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm")


val logger = LoggerFactory.getLogger("VendorPluginUtils")

operator fun LocalDate.rangeTo(endDate: LocalDate) =
    generateSequence(this) { it.plusDays(1) }.takeWhile { it <= endDate }

const val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"


fun getTimeStampString(date: DateTime): String {
    return timeStampFormatter.print(date)
}

fun getTimeStamp(string: String): DateTime {
    return timeStampFormatter.parseDateTime(string)
}

fun getDateString(date: DateTime): String {
    return dateFormatter.print(date)
}

fun LocalDate.toFormattedString(): String {
    return dateFormatter.print(this)
}

fun LocalTime.toFormattedString(): String {
    return timeFormatter.print(this)
}

fun getDate(string: String): DateTime {
    return dateFormatter.parseDateTime(string)
}

fun getTimeString(time: DateTime): String {
    return timeFormatter.print(time)
}

fun getTime(string: String): DateTime {
    return timeFormatter.parseDateTime(string)
}

fun Logger.selectAndLog(logMessage: String, exception: Exception) {
    val causes = getCause(exception)
    val log: (String, Throwable) -> Unit = if (causes.any { it is IIgnoreException })
        this::info // diagnostic-level for sentry is `warn` so changing this as to reduce clutter
    else
        this::error
    log(logMessage, exception)
}

fun <R> runBlockingForResult(block: suspend CoroutineScope.() -> R): Result<R> {
    return try {
        Result.success(wrapErrorInVendorPluginException { runBlocking { block() } })
    } catch (exception: VendorPluginException) {
        Result.error(exception)
    }
}

fun Throwable.toVendorPluginException(inventoryUnavailable: Boolean = false, errorMessage: String? = null): VendorPluginException {
    if (this is VendorPluginException) return this

    if (!errorMessage.isNullOrEmpty() && !inventoryUnavailable) {
        return VendorPluginException(errorMessage)
    } else if (!errorMessage.isNullOrEmpty() && inventoryUnavailable) {
        return InventoryUnavailableException(errorMessage, this)
    }

    val msg = when (this) {
        is HttpException -> {
            val errorBody = this.response()?.errorBody()!!.string()
            "Http Exception in Vendor Plugin with message: $errorBody"
        }
        else -> {
            "Exception Occurred: ${this.message}"
        }
    }
    return when {
        inventoryUnavailable -> InventoryUnavailableException(msg, this)
        else -> VendorPluginException(msg, this)
    }
}

suspend fun <R> wrapErrorInVendorPluginExceptionSuspending(errorTransformer: (Throwable) -> VendorPluginException? = { null },
                                                           block: suspend () -> R): R {
    return try {
        block()
    } catch (exception: Exception) {
        throw errorTransformer(exception) ?: exception.toVendorPluginException()
    }
}

fun <R> wrapErrorInVendorPluginException(errorTransformer: (Throwable) -> VendorPluginException? = { null }, block: () -> R): R {
    return try {
        block()
    } catch (exception: Exception) {
        throw errorTransformer(exception) ?: exception.toVendorPluginException()
    }
}

fun <R> runBlockingWrappingError(block: suspend CoroutineScope.() -> R): R {
    return wrapErrorInVendorPluginException { runBlocking { block() } }
}

fun generateNonce(nonceLength: Int): String {
    var nonce = ""
    for (i in 0..nonceLength) {
        nonce += chars[Math.floor(Math.random() * chars.length).toInt()]
    }

    return nonce
}

fun convertCamelToSnakeCaseMap(camelCaseMap: Map<String, Any>): Map<String, Any> {
    val snakeCaseMap = mutableMapOf<String, Any>()
    for ((key, value) in camelCaseMap) {
        val modifiedKey = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key)
        snakeCaseMap[modifiedKey] = value
    }

    return snakeCaseMap
}

fun applyThreshold(inventoryThreshold: IInventoryThreshold, slots: List<InventorySlotInfo>): List<InventorySlotInfo> {
    return slots.map {
        it.copy(
            inventoryInfo = it.inventoryInfo.map { inventoryInfoItem ->
                val finalRemaining = if(inventoryInfoItem.remaining > inventoryThreshold.threshold) inventoryInfoItem.remaining else 0
                inventoryInfoItem.copy(
                    remaining = finalRemaining,
                    availability = finalRemaining.availability()
                )
            }
        )
    }
}

fun <T> fetchAndUpdateSlotsPerQueryWrappingError(inventoryRangeQueries: List<InventoryRangeQuery<T>>,
                                                 headoutInventoryApi: IHeadoutInventoryApi,
                                                 block: suspend CoroutineScope.(query: InventoryRangeQuery<T>) -> List<InventorySlotInfo>) {
    val rangeResponses = runBlockingWrappingError {
        inventoryRangeQueries.map { query ->
            async {
                try {
                    wrapErrorInVendorPluginExceptionSuspending {
                        val slots = block(query)
                        InventoryRangeResponse(inventoryRangeQuery = query,
                            inventorySlots = when(query.productCode) {
                                is IInventoryThreshold -> applyThreshold(query.productCode as IInventoryThreshold, slots)
                                else -> slots
                            })
                    }
                } catch (exception: VendorPluginException) {
                    logger.selectAndLog("Inventory fetch failed for Query: $query", exception)
                    PluginNotificationManager.notify(SlackChannel.ALERT_INVENTORY_AUTOMATION, "Inventory fetch " +
                            "failed for Query: $query", exception)
                    null
                }
            }
        }.mapNotNull { it.await() }
    }
    // Do all the DB operations that could result in an interrupt outside the coroutine
    rangeResponses.forEach {
        try {
            wrapErrorInVendorPluginException { headoutInventoryApi.updateInventoryForRange(it) }
        } catch (exception: VendorPluginException) {
            logger.selectAndLog("Inventory update in database failed for Query: ${it.inventoryRangeQuery}", exception)
            PluginNotificationManager.notify(SlackChannel.ALERT_INVENTORY_AUTOMATION, "Inventory update in DB " +
                    "failed for Query: ${it.inventoryRangeQuery}", exception)
        }
    }
}

fun groupAndAccumulateInventorySlotInfoResponses(responseList: List<InventorySlotInfo>, accumulateInventoryInfo: Boolean = true, accumulatePriceInfo: Boolean = true): List<InventorySlotInfo> {
    return responseList
        .groupBy { it.date.toLocalDateTime(it.startTime) }
        .mapValues {
            it.value.foldRight(
                InventorySlotInfo(
                    date = it.key.toLocalDate(),
                    startTime = it.key.toLocalTime(),
                    inventoryInfo = emptyList(),
                    inventoryPriceInfo = emptyList()
                )
            ) {obj, acc ->
                obj.copy(
                    inventoryInfo = if(accumulateInventoryInfo){
                        obj.inventoryInfo + acc.inventoryInfo
                    }else{
                        obj.inventoryInfo
                    },
                    inventoryPriceInfo = if(accumulatePriceInfo){
                        obj.inventoryPriceInfo + acc.inventoryPriceInfo
                    }else{
                        obj.inventoryPriceInfo
                    }


                )
            }
        }
        .map { it.value }
}
suspend fun <T> retryIO(
    errorCode: Int,
    errorCodeChecker: (Throwable, Int) -> Boolean,
    times: Int = 5,
    initialDelay: Long = 100, // 0.1 second
    maxDelay: Long = 5000,    // 5 second
    factor: Double = 2.0,
    condition: suspend () -> Boolean = { true },
    block: suspend () -> T): T {
    var currentDelay = initialDelay
    repeat(times) {
        println("Current Delay: $currentDelay time: $it")
        try {
            if (condition.invoke()) {
                return block()
            }
        } catch (e: Exception) {
            if (!errorCodeChecker(e, errorCode)) {
                throw e
            }
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // last attempt
}

fun getCause(e: Throwable): List<Throwable> {
    var cause: Throwable?
    val result = mutableListOf(e)
    var counter = 0;
    while (e.cause.also { cause = it } != null && counter < 10) {
        cause?.let { result.add(it) }
        counter += 1
    }

    return result
}

fun Int.availability(): InventoryAvailability = if (this > 0) InventoryAvailability.LIMITED else InventoryAvailability.CLOSED