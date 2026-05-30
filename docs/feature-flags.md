# Feature Flags

## Purpose

Feature Flags allow the team to enable or disable specific functionalities during internal testing without modifying multiple files or releasing a new build.

---

## Implementation

Flags live in `util/FeatureFlags.kt` and are backed by `MutableStateFlow` so the UI reacts instantly when a flag is toggled — no app restart or screen navigation required.

```kotlin
object FeatureFlags {

    private val _enableCreateTicket = MutableStateFlow(true)
    val enableCreateTicket: StateFlow<Boolean> = _enableCreateTicket.asStateFlow()

    private val _enablePriorityUpdate = MutableStateFlow(true)
    val enablePriorityUpdate: StateFlow<Boolean> = _enablePriorityUpdate.asStateFlow()

    fun setCreateTicket(enabled: Boolean) { _enableCreateTicket.value = enabled }
    fun setPriorityUpdate(enabled: Boolean) { _enablePriorityUpdate.value = enabled }
}
```

---

## Settings Screen

Flags are exposed in a dedicated **Settings screen** (`ui/screens/settings/SettingsScreen.kt`), accessible via the gear icon (⚙) in the Ticket List top bar.

Each toggle calls the corresponding setter on `FeatureFlags` directly. Because the UI collects the `StateFlow`, affected composables recompose automatically.

---

## Usage in the UI

Flags are collected as Compose state at the exact point of use:

**`TicketListScreen` — Create Ticket FAB:**
```kotlin
val createTicketEnabled by FeatureFlags.enableCreateTicket.collectAsStateWithLifecycle()

if (createTicketEnabled) {
    FloatingActionButton(onClick = onCreateTicket) { ... }
}
```

**`TicketDetailScreen` — Priority dropdown:**
```kotlin
val priorityUpdateEnabled by FeatureFlags.enablePriorityUpdate.collectAsStateWithLifecycle()

PriorityDropdown(
    current = ticket.priority,
    onSelected = { viewModel.updatePriority(ticket.id, it) },
    enabled = priorityUpdateEnabled
)
```

No wrapper components, no abstraction layers — the flag is read directly where the decision is made.

---

## How to Evolve This

When the product moves to production, flags can be migrated to **Firebase Remote Config** by only modifying `FeatureFlags.kt`:

```kotlin
object FeatureFlags {
    val enableCreateTicket: StateFlow<Boolean>
        get() = remoteConfigFlow("enable_create_ticket")

    val enablePriorityUpdate: StateFlow<Boolean>
        get() = remoteConfigFlow("enable_priority_update")
}
```

All call sites in the UI remain unchanged — they only reference `FeatureFlags.X` and do not care about the source of the value. This is the key design decision: **centralized definition, distributed reactive usage.**
