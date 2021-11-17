package com.headout.vendor.plugins.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.LettuceFutures
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.coroutines
import org.joda.time.Weeks
import java.time.Duration

class RedisKeyNotFoundException(key: String) : Exception("Key $key not found in redis or backing call")

class RedisManager private constructor(private val redisClient: RedisClient) {

    private val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), JodaModule())
            .setDateFormat(StdDateFormat().withColonInTimeZone(true))

    private suspend fun <R> useClient(block: suspend StatefulRedisConnection<String, String>.() -> R) : R {
        return redisClient.connect().use { block(it) }
    }

    private fun <R> useClientBlocking(block: StatefulRedisConnection<String, String>.() -> R) : R {
        return redisClient.connect().use { block(it) }
    }

    @ExperimentalLettuceCoroutinesApi
    suspend fun <T> setObject(key: String, data: T) {
        val string = objectMapper.writeValueAsString(data)
        useClient {  coroutines().set(key, string) }
    }

    @ExperimentalLettuceCoroutinesApi
    suspend fun <T> getObject(key: String, objectClass: Class<T>): T? {
        val string: String? = useClient {  coroutines().get(key) }
        return string?.let { objectMapper.readValue(it, objectClass) }
    }

    fun incrementCounter(key: String): Long? {
        return useClientBlocking { sync().incr(key) }
    }

    fun <T> getObjectBlocking(key: String, objectClass: Class<T>): T? {
        val string: String? = useClientBlocking { sync().get(key) }
        return string?.let { objectMapper.readValue(it, objectClass) }

    }

    @ExperimentalLettuceCoroutinesApi
    suspend fun <T> getOrSetBacking(redisKey: String,
                                    classObject: Class<T>,
                                    expiryInSeconds: Int = Weeks.ONE.toStandardSeconds().seconds,
                                    block: suspend () -> Map<String, T>): T {

        return getObject(redisKey, classObject) ?: run {
            val result = block()
            storeAndGetResult(redisKey, expiryInSeconds, result)
        }
    }

    @ExperimentalLettuceCoroutinesApi
    fun <T> getOrSetBackingBlocking(redisKey: String,
                                    classObject: Class<T>,
                                    expiryInSeconds: Int = Weeks.ONE.toStandardSeconds().seconds,
                                    block: () -> Map<String, T>): T {

        return getObjectBlocking(redisKey, classObject) ?: run {
            val result = block()
            storeAndGetResult(redisKey, expiryInSeconds, result)
        }
    }

    private fun <T> storeAndGetResult(redisKey: String,
                                      expiryInSeconds: Int = Weeks.ONE.toStandardSeconds().seconds,
                                      result: Map<String, T>): T {
        useClientBlocking {
            val commands = async()
            commands.setAutoFlushCommands(false)
            val futures = result.flatMap {
                val stringValue = objectMapper.writeValueAsString(it.value)
                listOf(commands.set(it.key, stringValue), commands.expire(it.key, expiryInSeconds.toLong()))
            }

            commands.flushCommands()
            LettuceFutures.awaitAll(Duration.ofSeconds(5), *futures.toTypedArray())
        }
        return result[redisKey] ?: throw RedisKeyNotFoundException(redisKey)
    }

    companion object {
        fun getInstance(host: String): RedisManager = RedisManager(RedisClient.create("redis://$host"))
    }
}
