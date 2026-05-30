package com.panini.support.data.remote

import com.panini.support.data.remote.model.CreateTicketRequest
import com.panini.support.data.remote.model.TicketDto
import com.panini.support.data.remote.model.UpdatePriorityRequest
import com.panini.support.data.remote.model.UpdateStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Retrofit API contract.
 * Defines all endpoints the app would consume once the backend is live.
 * Currently unused — the app runs on MockTickets via TicketRepository.
 */
interface ApiService {

    @GET("tickets")
    suspend fun getTickets(): Response<List<TicketDto>>

    @GET("tickets/{id}")
    suspend fun getTicketById(@Path("id") id: String): Response<TicketDto>

    @POST("tickets")
    suspend fun createTicket(@Body request: CreateTicketRequest): Response<TicketDto>

    @PUT("tickets/{id}/status")
    suspend fun updateTicketStatus(
        @Path("id") id: String,
        @Body request: UpdateStatusRequest
    ): Response<TicketDto>

    @PUT("tickets/{id}/priority")
    suspend fun updateTicketPriority(
        @Path("id") id: String,
        @Body request: UpdatePriorityRequest
    ): Response<TicketDto>
}
