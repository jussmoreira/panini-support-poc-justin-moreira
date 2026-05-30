package com.panini.support.ui.components

import androidx.compose.ui.graphics.Color
import com.panini.support.data.model.Status

/**
 * Single source of truth for the semantic color of each ticket [Status].
 * Keyed off the enum (not the display label) so it stays correct if labels change.
 * Shared by the list card and the detail screen to avoid duplication.
 */
fun statusColor(status: Status): Color = when (status) {
    Status.OPEN -> Color(0xFF2980B9)        // Blue
    Status.IN_PROGRESS -> Color(0xFFD35400) // Orange
    Status.RESOLVED -> Color(0xFF27AE60)    // Green
    Status.CLOSED -> Color(0xFF7F8C8D)      // Gray
}
