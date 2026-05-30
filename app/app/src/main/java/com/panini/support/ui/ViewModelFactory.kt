package com.panini.support.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.panini.support.data.repository.TicketRepository
import com.panini.support.ui.screens.createticket.CreateTicketViewModel
import com.panini.support.ui.screens.ticketdetail.TicketDetailViewModel
import com.panini.support.ui.screens.ticketlist.TicketListViewModel

/**
 * Factory to instantiate ViewModels with the required TicketRepository dependency.
 */
class ViewModelFactory(
    private val ticketRepository: TicketRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TicketListViewModel::class.java) -> {
                TicketListViewModel(ticketRepository) as T
            }
            modelClass.isAssignableFrom(TicketDetailViewModel::class.java) -> {
                TicketDetailViewModel(ticketRepository) as T
            }
            modelClass.isAssignableFrom(CreateTicketViewModel::class.java) -> {
                CreateTicketViewModel(ticketRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
