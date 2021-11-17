package com.headout.vendor.plugins.ho.api

import com.headout.vendor.plugins.ho.api.models.*
import retrofit2.http.*

interface hoApi {

    @POST("login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("clients")
    suspend fun getClients(): ClientList

    /**
     * Get list of client products
     *
     * @param pos The point of sale ID of the requested client
     */
    @GET("products")
    suspend fun getProducts(@Header("pos") posId: Long): ProductList

    /**
     * Get product detailed info
     *
     * @param pos The point of sale ID of the requested client
     * @param productId The ID of the product
     */
    @GET("products/{productId}")
    suspend fun getProduct(@Header("pos") posId: Long,
                           @Path("productId") productId: Long): ProductDetail


    @GET("products/{productId}/availability")
    suspend fun getProductAvailability(@Header("pos") posId: Long,
                               @Path("productId") productId: Long,
                               @Query("month") month: String,
                               @Query("year") year: String): String

    /**
     * Get product events
     *
     * @param pos The point of sale ID of the requested client
     * @param productId The ID of the product
     * @param date The date of the events in dd/MM/yyyy format
     * @param toDate Limit date of the events in dd/MM/yyyy format (used to search events between 'date' and 'toDate')
     */
    @GET("products/{productId}/events")
    suspend fun getProductsEvents(
            @Header("pos") posId: Long,
            @Path("productId") productId: Long,
            @Query("date") date: String,
            @Query("toDate") toDate: String? = null
    ): VenueList

    /**
     * Get product pricing
     *
     * @param pos The point of sale ID of the requested client
     * @param productId The ID of the product
     * @param date The date of the pricing to query in dd/MM/yyyy format
     */
    @GET("products/{productId}/pricing")
    suspend fun getProductsPricing(@Header("pos") posId: Int,
                                @Path("productId") productId: Int,
                                @Query("date") date: String): BuyerTypeList

    /**
     * Get purchase info
     *
     * @param pos The point of sale ID of the requested client
     * @param webCookie UUID Version 4 that identifies each purchase process from the beginning to the end. It can't be used more than once for different processes and it is required even for getting the products, there you are already in a purchase process.
     */
    @GET("purchase")
    suspend fun getPurchase(): PurchaseResponse

    /**
     * Confirm purchase
     *
     * @param pos The point of sale ID of the requested client
     * @param webCookie UUID Version 4 that identifies each purchase process from the beginning to the end. It can't be used more than once for different processes and it is required even for getting the products, there you are already in a purchase process.
     */
    @PUT("purchase")
    suspend fun confirmPurchase(
            @Header("pos") posId: Long,
            @Header("webCookie") uuid: String,
            @Query("paymentMethod") paymentMethod: String? = "prepayment"): PaymentResponse

    /**
     * Delete purchase
     *
     * @param pos The point of sale ID of the requested client
     * @param webCookie UUID Version 4 that identifies each purchase process from the beginning to the end. It can't be used more than once for different processes and it is required even for getting the products, there you are already in a purchase process.
     */
    @DELETE("purchase")
    suspend fun DeletePurchase(): Unit

    /**
     * Add reservation
     *
     * @param pos The point of sale ID of the requested client
     * @param webCookie UUID Version 4 that identifies each purchase process from the beginning to the end. It can't be used more than once for different processes and it is required even for getting the products, there you are already in a purchase process.
     * @param body Parameters of the product reservation
     */
    @POST("reservation")
    suspend fun addReservation(@Header("pos") posId: Long,
                               @Header("webCookie") uuid: String,
                               @Body reservationRequest: ReservationRequest): ReservationResponse

    /**
     * Delete reservation
     *
     * @param pos The point of sale ID of the requested client
     * @param webCookie UUID Version 4 that identifies each purchase process from the beginning to the end. It can't be used more than once for different processes and it is required even for getting the products, there you are already in a purchase process.
     */
    @DELETE("reservation/{reservationId}")
    suspend fun deleteReservation(@Header("pos") posId: Int, @Path("reservationId") reservationId: String): Unit

    /**
     * Reschedule a reservation
     *
     * @param pos The point of sale ID of the requested client
     * @param webCookie UUID Version 4 that identifies each purchase process from the beginning to the end. It can't be used more than once for different processes and it is required even for getting the products, there you are already in a purchase process.
     * @param body Parameters of the product reservation reschedule
     */
    @PUT("reservation/{reservationId}/reschedule")
    suspend fun rescheduleReservation(@Path("reservationId") reservationId: String, @Body body: RescheduleRequest): ReservationResponse
}
