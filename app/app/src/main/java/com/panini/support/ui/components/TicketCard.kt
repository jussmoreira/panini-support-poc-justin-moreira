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
import com.panini.support.data.model.Priority
import com.panini.support.data.model.Ticket
import java.time.format.DateTimeFormatter

/**
 * Reusable card composable for displaying a ticket summary in any list.
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = ticket.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                PriorityBadge(priority = ticket.priority)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = ticket.provider,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = ticket.category.label,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = ticket.status.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor(ticket.status.label)
                )
                Text(
                    text = ticket.createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun PriorityBadge(priority: Priority) {
    val color = when (priority) {
        Priority.CRITICAL -> Color(0xFFD32F2F)
        Priority.HIGH     -> Color(0xFFF57C00)
        Priority.MEDIUM   -> Color(0xFFF9A825)
        Priority.LOW      -> Color(0xFF388E3C)
    }
    Text(
        text = priority.label.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

private fun statusColor(status: String): Color = when (status) {
    "Open"        -> Color(0xFF1565C0)
    "In Progress" -> Color(0xFFF57C00)
    "Resolved"    -> Color(0xFF388E3C)
    "Closed"      -> Color(0xFF757575)
    else          -> Color.Unspecified
}
