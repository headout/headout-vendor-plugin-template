package com.headout.vendor.plugin.ho.api

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.slf4j.LoggerFactory

import java.io.IOException

internal class DateFromStringDeserializer : JsonDeserializer<LocalDateTime>() {
     private val logger = LoggerFactory.getLogger(DateFromStringDeserializer::class.java)

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LocalDateTime? {
        val timestamp = jp.text.trim { it <= ' ' }

        return try {
            LocalDateTime((timestamp + "000").toLong(), DateTimeZone.UTC)
        } catch (e: NumberFormatException) {
            logger.warn("Unable to deserialize timestamp: $timestamp", e)
            null
        }
    }
}