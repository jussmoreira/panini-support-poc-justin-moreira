package com.panini.support.ui.screens.ticketlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panini.support.data.repository.TicketEvent
import com.panini.support.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Ticket List screen.
 *
 * Event-based communication:
 * - Collects [TicketRepository.tickets] (StateFlow) to always render the latest list.
 * - Subscribes to [TicketRepository.events] (SharedFlow) to react to ticket creation
 *   and priority updates without manual screen refreshes (Exam req. 5.1).
 */
class TicketListViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketListUiState(isLoading = true))
    val uiState: StateFlow<TicketListUiState> = _uiState.asStateFlow()

    init {
        observeTickets()
        observeEvents()
    }

    // ─── Reactive list ────────────────────────────────────────────────────────
    private fun observeTickets() {
        viewModelScope.launch {
            ticketRepository.tickets.collect { tickets ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tickets = tickets
                )
            }
        }
    }

    // ─── Event bus ────────────────────────────────────────────────────────────
    // SharedFlow events allow this screen to react to changes triggered from
    // other screens (e.g., CreateTicket or TicketDetail) automatically.
    private fun observeEvents() {
        viewModelScope.launch {
            ticketRepository.events.collect { event ->
                when (event) {
                    is TicketEvent.TicketCreated -> {
                        _uiState.value = _uiState.value.copy(
                            snackbarMessage = "Ticket \"${event.ticket.title}\" created"
                        )
                    }
                    is TicketEvent.PriorityUpdated -> {
                        // List re-sorted in repository; StateFlow handles the update.
                    }
                    is TicketEvent.StatusUpdated -> {
                        // No additional action needed at list level.
                    }
                }
            }
        }
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }
}
