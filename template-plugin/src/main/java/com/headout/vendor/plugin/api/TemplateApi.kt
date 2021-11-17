package com.headout.vendor.plugin.api

import com.headout.vendor.plugins.experticket.models.request.ReservationRequest
import com.headout.vendor.plugins.experticket.models.request.TransactionRequest
import com.headout.vendor.plugins.experticket.models.response.*
import retrofit2.http.*

// Example API docs at https://apidocs.experticket.com/
interface TemplateApi {
    @GET("/api/cataloglastupdateddatetime")
    suspend fun lastUpdates(): LastUpdated

    @GET("/api/catalog")
    suspend fun getCatalog(): Catalog

    @GET("/api/sessions")
    suspend fun getSessions(
        @Query("SessionsGroupProfileIds") sessionGroupProfileId: List<String>,
        @Query("Dates") dates: List<String>
    ): SessionResponse

    @GET("/api/availablecapacity")
    suspend fun getAvailability(
        @Query("ProductIds") productId: String,
        @Query("Dates") dates: List<String>
    ): AvailabilityResponse


    @POST("/api/reservation")
    suspend fun bookReservation(
        @Body reservationBody: ReservationRequest
    ): ReservationResponse

    @POST("/api/transaction")
    suspend fun makeTransaction(
        @Body transactionBody: TransactionRequest
    ): Transaction

    @GET("/api/RealTimePrices")
    suspend fun getRealTimePrices(
        @Query("ProductIds") productId: String,
        @Query("AccessDates") accessDates: List<String>
    ): RealTimePrice
}