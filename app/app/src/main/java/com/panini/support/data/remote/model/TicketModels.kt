package com.panini.support.data.remote.model

import com.panini.support.data.model.Category
import com.panini.support.data.model.Priority
import com.panini.support.data.model.Status
import com.panini.support.data.model.Ticket
import java.time.LocalDateTime

// ─── Response DTOs ───────────────────────────────────────────────────────────

/**
 * Data Transfer Object for a ticket received from the API.
 * Field names match the JSON contract defined in /contracts/tickets-api.yaml.
 */
data class TicketDto(
    val id: String,
    val title: String,
    val description: String,
    val priority: String,
    val status: String,
    val provider: String,
    val category: String,
    val createdAt: String,
    val assignedTo: String?
)

// ─── Request bodies ───────────────────────────────────────────────────────────

data class CreateTicketRequest(
    val title: String,
    val description: String,
    val priority: String,
    val provider: String,
    val category: String
)

data class UpdateStatusRequest(
    val status: String
)

data class UpdatePriorityRequest(
    val priority: String
)

// ─── Mapper ───────────────────────────────────────────────────────────────────

/**
 * Converts a network DTO into a clean domain model.
 * Boundary point: parsing happens here, not in the ViewModel or UI.
 */
fun TicketDto.toDomain(): Ticket = Ticket(
    id = id,
    title = title,
    description = description,
    priority = Priority.valueOf(priority.uppercase()),
    status = Status.valueOf(status.uppercase().replace(" ", "_")),
    provider = provider,
    category = Category.valueOf(category.uppercase()),
    createdAt = LocalDateTime.parse(createdAt),
    assignedTo = assignedTo
)
