package com.headout.vendor.plugins.ho.api.models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.headout.vendor.plugins.ho.api.DateFromStringDeserializer
import org.joda.time.LocalDateTime


data class Event(
        val eventId: Long,
        @JsonDeserialize(using=DateFromStringDeserializer::class)
        val startDatetime: LocalDateTime,
        @JsonDeserialize(using=DateFromStringDeserializer::class)
        val endDatetime: LocalDateTime,
        val capacity: Long,
        val totalAvailability: Int,
        val status: String,
        val numbered: String?,
        val isSpecial: String?
)
