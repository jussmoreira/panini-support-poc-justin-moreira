package com.panini.support.data.repository

import com.panini.support.data.mock.MockTickets
import com.panini.support.data.model.Category
import com.panini.support.data.model.Priority
import com.panini.support.data.model.Status
import com.panini.support.data.model.Ticket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.util.UUID

/**
 * Events emitted when important actions occur in the system.
 * ViewModels subscribe to this flow to react without polling.
 */
sealed class TicketEvent {
    data class TicketCreated(val ticket: Ticket) : TicketEvent()
    data class StatusUpdated(val ticketId: String, val newStatus: Status) : TicketEvent()
    data class PriorityUpdated(val ticketId: String, val newPriority: Priority) : TicketEvent()
}

/**
 * Single source of truth for ticket data in this PoC.
 *
 * - [tickets] (StateFlow): UI always reads the current sorted list.
 * - [events] (SharedFlow): broadcast channel for one-time actions.
 *   When the backend is ready, replace the mock calls with ApiService
 *   calls — the StateFlow/SharedFlow contract stays the same.
 */
object TicketRepository {

    private val _tickets = MutableStateFlow<List<Ticket>>(MockTickets.tickets)
    val tickets: StateFlow<List<Ticket>> = _tickets.asStateFlow()

    private val _events = MutableSharedFlow<TicketEvent>()
    val events: SharedFlow<TicketEvent> = _events.asSharedFlow()

    // ─── Read ────────────────────────────────────────────────────────────────

    fun getTicketById(id: String): Ticket? =
        _tickets.value.find { it.id == id }

    // ─── Write ───────────────────────────────────────────────────────────────

    suspend fun createTicket(
        title: String,
        description: String,
        priority: Priority,
        provider: String,
        category: Category
    ): ApiResult<Ticket> {
        val newTicket = Ticket(
            id = "TKT-${UUID.randomUUID().toString().take(4).uppercase()}",
            title = title,
            description = description,
            priority = priority,
            status = Status.OPEN,
            provider = provider,
            category = category,
            createdAt = LocalDateTime.now()
        )
        // Update the list sorted by priority so Scenario 2 stays consistent
        _tickets.value = (_tickets.value + newTicket).sortedBy { it.priority.ordinal }
        _events.emit(TicketEvent.TicketCreated(newTicket))
        return ApiResult.Success(newTicket)
    }

    suspend fun updateStatus(ticketId: String, newStatus: Status): ApiResult<Unit> {
        _tickets.value = _tickets.value.map { ticket ->
            if (ticket.id == ticketId) ticket.copy(status = newStatus) else ticket
        }
        _events.emit(TicketEvent.StatusUpdated(ticketId, newStatus))
        return ApiResult.Success(Unit)
    }

    suspend fun updatePriority(ticketId: String, newPriority: Priority): ApiResult<Unit> {
        _tickets.value = _tickets.value
            .map { ticket ->
                if (ticket.id == ticketId) ticket.copy(priority = newPriority) else ticket
            }
            .sortedBy { it.priority.ordinal } // Re-sort so higher priority floats up
        _events.emit(TicketEvent.PriorityUpdated(ticketId, newPriority))
        return ApiResult.Success(Unit)
    }
}
