package com.panini.support.data

import com.panini.support.data.remote.ApiService
import com.panini.support.data.remote.RetrofitClient
import com.panini.support.data.repository.TicketRepository

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val ticketRepository: TicketRepository
}

/**
 * Default implementation of the AppContainer.
 * Provides Retrofit API Service and Ticket Repository instances.
 */
class DefaultAppContainer : AppContainer {

    private val apiService: ApiService by lazy {
        RetrofitClient.apiService
    }

    override val ticketRepository: TicketRepository by lazy {
        TicketRepository(apiService)
    }
}
