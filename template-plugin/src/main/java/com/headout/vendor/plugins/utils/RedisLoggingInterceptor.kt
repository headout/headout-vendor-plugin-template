package com.headout.vendor.plugins.utils

import okhttp3.Interceptor
import okhttp3.Response
import org.joda.time.LocalDateTime


class RedisLoggingInterceptor(private val redisManager: RedisManager): Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestUrl = chain.request().url().url().toExternalForm()
        val now = LocalDateTime.now()
        val date = now.toString("dd-MM-yyyy")
        val hour = now.hourOfDay
        val minute = now.minuteOfHour
        val redisKey = "$date $hour-$minute $requestUrl"

        redisManager.incrementCounter(key = redisKey)
        return chain.proceed(chain.request())
    }

}