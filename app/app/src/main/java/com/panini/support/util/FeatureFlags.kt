package com.panini.support.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Feature Flags for the Panini Support PoC.
 *
 * Flags are backed by MutableStateFlow so the UI reacts instantly when a flag
 * is toggled from the Settings screen — no restart required.
 *
 * In a production phase these could be replaced by remote values
 * (e.g. Firebase Remote Config) without touching any screen logic:
 * only this object would change.
 */
object FeatureFlags {

    private val _enableCreateTicket = MutableStateFlow(true)
    /** Controls whether support agents can create new tickets from the app. */
    val enableCreateTicket: StateFlow<Boolean> = _enableCreateTicket.asStateFlow()

    private val _enablePriorityUpdate = MutableStateFlow(true)
    /** Controls whether ticket priority can be updated from the detail screen. */
    val enablePriorityUpdate: StateFlow<Boolean> = _enablePriorityUpdate.asStateFlow()

    fun setCreateTicket(enabled: Boolean) {
        _enableCreateTicket.value = enabled
    }

    fun setPriorityUpdate(enabled: Boolean) {
        _enablePriorityUpdate.value = enabled
    }
}
