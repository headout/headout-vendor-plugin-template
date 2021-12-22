package com.headout.vendor.plugin

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.headout.vendor.api.exceptions.VendorPluginException
import com.headout.vendor.plugins.template.api.TemplateApi
import com.headout.vendor.plugins.template.models.requests.CancelCompleteReservation
import com.headout.vendor.plugins.template.models.requests.CreateCompleteReservation
import com.headout.vendor.plugins.template.models.requests.GetAvailability
import com.headout.vendor.plugins.template.models.requests.GetTicketTypes
import com.headout.vendor.plugins.template.models.responses.*

val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

/*
* Miscellaneous helper methods.
* */
class TemplateApiWrapper(_credentials: TemplateCredentials) {
    private val api: TemplateApi = TemplatePluginHelper(_credentials).templateApi

    suspend fun getAvailableUsers(): List<Users> {
        val body = api.getAvailableUsers()
        if (body.success) {
            return body.users
        } else throw VendorPluginException("getAvailableUsers failed")
    }

    suspend fun getTicketTypes(params: GetTicketTypes): GetTicketTypesResponse {
        val body = api.getTicketTypes(params)
        if (body.success) {
            return body
        } else throw VendorPluginException("getAvailableUsers failed")
    }

    // returns something like
    // {2021-12-01={10:00=true, 10:15=true, 10:30=true, 10:45=true, 11:00=true, 11:15=true, 11:30=true, 11:45=true
    suspend fun getAvailability(params: GetAvailability): GetAvailabilityResponse {
        val body = api.getAvailability(params)
        if (body.success) {
            return body
        } else throw VendorPluginException("getAvailableUsers failed")
    }

    suspend fun createCompleteReservation(params: CreateCompleteReservation): CreateCompleteReservationResponse {
        val body = api.createCompleteReservation(params)
        if (body.success) {
            return body
        } else throw VendorPluginException("createCompleteReservation failed")
    }

    suspend fun cancelCompleteReservation(params: CancelCompleteReservation): CancelCompleteReservationResponse {
        val body = api.cancelCompleteReservation(params)
        if (body.success) {
            return body
        } else throw VendorPluginException("cancelCompleteReservation failed")
    }
}
