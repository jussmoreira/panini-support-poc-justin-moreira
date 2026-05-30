package com.panini.support.util

/**
 * Feature Flags for the Panini Support PoC.
 *
 * Allows the team to enable/disable features quickly during internal testing
 * without modifying multiple parts of the system.
 *
 * In a future phase these could be fetched from a remote config service
 * (e.g., Firebase Remote Config) — the UI already reads from this object,
 * so only this file would need to change.
 */
object FeatureFlags {
    /** Controls whether support agents can create new tickets from the app. */
    const val ENABLE_CREATE_TICKET = true

    /** Controls whether ticket priority can be updated from the detail screen. */
    const val ENABLE_PRIORITY_UPDATE = true
}
