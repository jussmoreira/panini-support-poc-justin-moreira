package com.panini.support.ui.screens.ticketlist

import com.panini.support.data.model.Ticket

data class TicketListUiState(
    val isLoading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val errorMessage: String? = null
)
