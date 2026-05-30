# Event-Based Communication

## Strategy

The application uses `StateFlow` and `SharedFlow` from Kotlin Coroutines to handle reactive updates across screens without manual reloads.

| Flow type | Purpose |
|-----------|---------|
| `StateFlow<List<Ticket>>` | Always holds the current ticket list — UI observes and recomposes automatically |
| `SharedFlow<TicketEvent>` | Emits discrete events (created, status updated, priority updated) |

**Why not callbacks or LiveData?**
`StateFlow` and `SharedFlow` are coroutine-native, lifecycle-aware when collected with `collectAsStateWithLifecycle()`, and require no additional dependencies. They decouple the emitter (Repository) from the observer (ViewModel) — the Repository does not know or care who is listening.

---

## Implementation

### Repository — source of truth

```kotlin
// Always holds the current sorted list
private val _tickets = MutableStateFlow(MockTickets.tickets.toList())
val tickets: StateFlow<List<Ticket>> = _tickets.asStateFlow()

// Emits discrete events
private val _ticketEvents = MutableSharedFlow<TicketEvent>()
val ticketEvents: SharedFlow<TicketEvent> = _ticketEvents.asSharedFlow()
```

### ViewModel — observes both

```kotlin
init {
    viewModelScope.launch {
        repository.tickets.collect { tickets ->
            _uiState.value = TicketListUiState.Success(tickets)
        }
    }
}
```

---

## Scenario 1 — Ticket Creation

```
User submits CreateTicketScreen
        ↓
CreateTicketViewModel.createTicket()
        ↓
TicketRepository.createTicket()
  → adds ticket to MockTickets list
  → updates _tickets StateFlow
  → emits TicketEvent.TicketCreated
        ↓
TicketListViewModel collects StateFlow
        ↓
LazyColumn recomposes → new ticket appears immediately
```

No manual navigation or screen reload needed.

---

## Scenario 2 — Priority Update

```
User changes priority in TicketDetailScreen
        ↓
TicketDetailViewModel.updatePriority()
        ↓
TicketRepository.updatePriority()
  → updates ticket in list
  → sorts list: HIGH → MEDIUM → LOW
  → updates _tickets StateFlow
  → emits TicketEvent.PriorityUpdated
        ↓
TicketListViewModel collects StateFlow
        ↓
LazyColumn recomposes → list reordered automatically
```

Sorting happens inside the Repository — business logic does not live in the UI layer.

---

## Sealed Event Class

```kotlin
sealed class TicketEvent {
    data class TicketCreated(val ticket: Ticket) : TicketEvent()
    data class StatusUpdated(val ticket: Ticket) : TicketEvent()
    data class PriorityUpdated(val ticket: Ticket) : TicketEvent()
}
```

---

## How to Evolve This

When a real backend is integrated:
- Repository operations become Retrofit calls — `StateFlow` updates after successful API response
- For real-time updates across devices, `SharedFlow` can be replaced with WebSockets or Firebase Realtime Database
- ViewModel and UI layers require zero changes
