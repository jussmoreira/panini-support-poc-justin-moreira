package com.panini.support.ui.screens.ticketdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panini.support.data.model.Priority
import com.panini.support.data.model.Status
import com.panini.support.data.model.Ticket
import com.panini.support.data.repository.ApiResult
import com.panini.support.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TicketDetailUiState(
    val ticket: Ticket? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class TicketDetailViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketDetailUiState())
    val uiState: StateFlow<TicketDetailUiState> = _uiState.asStateFlow()

    fun loadTicket(ticketId: String) {
        val ticket = ticketRepository.getTicketById(ticketId)
        _uiState.value = _uiState.value.copy(ticket = ticket)
    }

    fun updateStatus(ticketId: String, newStatus: Status) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, successMessage = null, errorMessage = null)
            when (val result = ticketRepository.updateStatus(ticketId, newStatus)) {
                is ApiResult.Success -> {
                    // Re-load ticket to reflect new status
                    val updated = ticketRepository.getTicketById(ticketId)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        ticket = updated,
                        successMessage = "Status updated to ${newStatus.label}"
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun updatePriority(ticketId: String, newPriority: Priority) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, successMessage = null, errorMessage = null)
            when (val result = ticketRepository.updatePriority(ticketId, newPriority)) {
                is ApiResult.Success -> {
                    val updated = ticketRepository.getTicketById(ticketId)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        ticket = updated,
                        successMessage = "Priority updated to ${newPriority.label}"
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(successMessage = null, errorMessage = null)
    }
}
