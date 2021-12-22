package com.headout.vendor.plugins.template.api

import com.headout.vendor.plugins.template.models.requests.*
import com.headout.vendor.plugins.template.models.responses.CancelCompleteReservationResponse
import com.headout.vendor.plugins.template.models.responses.CreateCompleteReservationResponse
import com.headout.vendor.plugins.template.models.responses.GetAvailabilityResponse
import com.headout.vendor.plugins.template.models.responses.GetAvailableUsersResponse
import com.headout.vendor.plugins.template.models.responses.GetTicketTypesResponse
import retrofit2.http.Body
import retrofit2.http.POST

// See API Docs provided by the Global Reseller at https://globalreseller.nl/documentation/api/getAvailableUsers
interface TemplateApi {
    @POST("getAvailableUsers")
    suspend fun getAvailableUsers(): GetAvailableUsersResponse

    @POST("getTicketTypes")
    suspend fun getTicketTypes(@Body request: GetTicketTypes): GetTicketTypesResponse

    @POST("getAvailability")
    suspend fun getAvailability(@Body request: GetAvailability): GetAvailabilityResponse

    @POST("createCompleteReservation")
    suspend fun createCompleteReservation(@Body request: CreateCompleteReservation): CreateCompleteReservationResponse

    @POST("cancelCompleteReservation")
    suspend fun cancelCompleteReservation(@Body request: CancelCompleteReservation): CancelCompleteReservationResponse
}
