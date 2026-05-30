package com.panini.support.data.model

import java.time.LocalDateTime

/**
 * Domain model for a support ticket.
 * Used by ViewModels and UI — completely framework-agnostic.
 */
data class Ticket(
    val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val status: Status,
    val provider: String,
    val category: Category,
    val createdAt: LocalDateTime,
    val assignedTo: String? = null
)

enum class Priority(val label: String) {
    CRITICAL("Critical"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

enum class Status(val label: String) {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    CLOSED("Closed")
}

enum class Category(val label: String) {
    INVENTORY("Inventory"),
    DISTRIBUTION("Distribution"),
    PROVIDER("Provider"),
    LOGISTICS("Logistics")
}
