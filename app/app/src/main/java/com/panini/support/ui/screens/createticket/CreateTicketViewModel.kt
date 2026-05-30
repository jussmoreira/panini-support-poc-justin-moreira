package com.panini.support.ui.screens.createticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panini.support.data.model.Category
import com.panini.support.data.model.Priority
import com.panini.support.data.model.Ticket
import com.panini.support.data.repository.ApiResult
import com.panini.support.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateTicketUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val createdTicket: Ticket? = null,
    val errorMessage: String? = null
)

class CreateTicketViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTicketUiState())
    val uiState: StateFlow<CreateTicketUiState> = _uiState.asStateFlow()

    fun createTicket(
        title: String,
        description: String,
        priority: Priority,
        provider: String,
        category: Category
    ) {
        if (title.isBlank() || provider.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Title and provider are required."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = TicketRepository.createTicket(title, description, priority, provider, category)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        createdTicket = result.data
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

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
