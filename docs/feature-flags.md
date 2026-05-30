# Feature Flags

## Purpose

Feature Flags allow the team to enable or disable specific functionalities during internal testing without modifying multiple files or releasing a new build.

---

## Implementation

All flags live in a single file: `util/FeatureFlags.kt`

```kotlin
object FeatureFlags {
    // Enables the Create Ticket button and screen access
    const val CREATE_TICKET_ENABLED = true

    // Enables priority updates from the ticket detail screen
    const val UPDATE_PRIORITY_ENABLED = true
}
```

**Why simple constants and not a more complex system?**
For a short-scope PoC with a small team, constants are readable, zero overhead, and immediately understandable by any engineer. Adding remote configuration at this stage would be premature optimization and disproportionate complexity — exactly what the project restrictions discourage.

---

## Usage in the UI

Flags are checked at the exact point of use:

**`TicketListScreen` — Create Ticket button:**
```kotlin
if (FeatureFlags.CREATE_TICKET_ENABLED) {
    FloatingActionButton(onClick = onCreateClick) {
        Icon(Icons.Default.Add, contentDescription = "Create ticket")
    }
}
```

**`TicketDetailScreen` — Priority selector:**
```kotlin
if (FeatureFlags.UPDATE_PRIORITY_ENABLED) {
    PrioritySelector(
        currentPriority = ticket.priority,
        onPriorityChange = { viewModel.updatePriority(it) }
    )
}
```

No wrapper components, no abstraction layers — the flag is read directly where the decision is made.

---

## How to Evolve This

When the product moves to production, flags can be migrated to **Firebase Remote Config** by only modifying `FeatureFlags.kt`:

```kotlin
object FeatureFlags {
    val CREATE_TICKET_ENABLED: Boolean
        get() = remoteConfig.getBoolean("create_ticket_enabled")

    val UPDATE_PRIORITY_ENABLED: Boolean
        get() = remoteConfig.getBoolean("update_priority_enabled")
}
```

All call sites in the UI remain unchanged — they only reference `FeatureFlags.X` and do not care about the source of the value. This is the key design decision: **centralized definition, distributed usage.**
