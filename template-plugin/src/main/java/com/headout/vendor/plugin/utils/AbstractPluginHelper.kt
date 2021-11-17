package com.headout.vendor.plugin.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.Weeks
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by @author Alpesh
 *
 */
abstract class AbstractPluginHelper {

	open val vendorPluginName: String = ""

	fun getOkHttpClientBuilder(isDebug: Boolean): OkHttpClient.Builder {
		val okHttpClientBuilder = OkHttpClient.Builder()
				.readTimeout(100, TimeUnit.SECONDS)
				.connectTimeout(100, TimeUnit.SECONDS)

		if (isDebug)
			okHttpClientBuilder.addInterceptor(getLoggingInterceptor(isDebug))
		return okHttpClientBuilder
	}

	private fun getLoggingInterceptor(isDebug: Boolean = false) = HttpLoggingInterceptor()
			.setLevel(if(isDebug){
				HttpLoggingInterceptor.Level.BODY
			} else{
				HttpLoggingInterceptor.Level.BASIC
			})

	open fun getObjectMapper(): ObjectMapper = ObjectMapper().apply {
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		registerModules(KotlinModule(), JodaModule())
	}

	open fun getRetrofit(baseUrl: String, client: OkHttpClient? = null):Retrofit {
		val retrofitBuilder = Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())

		return client?.let { retrofitBuilder.client(client).build() }?:retrofitBuilder.build()
	}

	fun getSetAuthToken(
		generateAuthToken: () -> String,
		redis: RedisManager,
		expiresInMinutes: Int = Weeks.ONE.toStandardMinutes().minutes
	): String {
		val authTokenKey = vendorPluginName + "_VENDOR_PLUGIN_AUTH_TOKEN"
		val authToken = runBlockingWrappingError {
			redis.getOrSetBacking(authTokenKey, String::class.java, TimeUnit.MINUTES.toSeconds(expiresInMinutes.toLong()).toInt()) {
				mapOf(Pair(authTokenKey, generateAuthToken()))
			}
		}
		return authToken
	}
}