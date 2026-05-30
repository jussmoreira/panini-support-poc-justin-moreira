package com.panini.support.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.panini.support.data.model.Priority
import com.panini.support.data.model.Ticket
import java.time.format.DateTimeFormatter

/**
 * Reusable card composable for displaying a ticket summary in any list.
 * Refactored with strict visual hierarchy, clear grids, and beautiful indicators.
 */
@Composable
fun TicketCard(
    ticket: Ticket,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 1. HEADER ROW: Creation Date & Priority Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Creation Date with explicit label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Created:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = ticket.createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                PriorityBadge(priority = ticket.priority)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. HERO CONTENT: Title on its own dedicated line (never squished)
            Text(
                text = ticket.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. FOOTER ROW: Explicit Status with Colored Dot Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Explicit label for Status
                    Text(
                        text = "Status:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                    // Modern Status Colored Dot
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusColor(ticket.status.label))
                    )
                    
                    Text(
                        text = ticket.status.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }


            }
        }
    }
}

/**
 * Premium pill-shaped badge for Priority with soft, readable colors.
 */
@Composable
private fun PriorityBadge(priority: Priority) {
    val (backgroundColor, textColor, borderColor) = when (priority) {
        Priority.CRITICAL -> Triple(Color(0xFFFADBD8), Color(0xFF78281F), Color(0xFFF5B7B1)) // Soft Red
        Priority.HIGH     -> Triple(Color(0xFFFDEBD0), Color(0xFF7E5109), Color(0xFFF8C471)) // Soft Orange
        Priority.MEDIUM   -> Triple(Color(0xFFFCF3CF), Color(0xFF7D6608), Color(0xFFF7DC6F)) // Soft Yellow
        Priority.LOW      -> Triple(Color(0xFFD5F5E3), Color(0xFF1E8449), Color(0xFF2ECC71).copy(alpha = 0.4f)) // Soft Green
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = priority.label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    }
}

/**
 * Returns solid color for the status dot indicator.
 */
private fun statusColor(status: String): Color = when (status) {
    "Open"        -> Color(0xFF2980B9) // Solid Blue
    "In Progress" -> Color(0xFFD35400) // Solid Orange
    "Resolved"    -> Color(0xFF27AE60) // Solid Green
    "Closed"      -> Color(0xFF7F8C8D) // Solid Gray
    else          -> Color.Unspecified
}
