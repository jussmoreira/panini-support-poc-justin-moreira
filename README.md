# Panini Support PoC — Justin Moreira

Mobile proof-of-concept for centralizing internal support ticket management related to Panini FIFA World Cup 2026 album distribution in Costa Rica.

## Overview

Replaces informal email/spreadsheet workflows with a structured ticket system covering supplier incidents, inventory shortages, distribution errors, and logistics issues across points of sale.

Built as a PoC with mock data, but the networking layer is structured to integrate with a real backend without reorganizing the project.

## Screens

| Screen | Description |
|---|---|
| Login | Simulated authentication |
| Ticket List | Reactive list sorted by priority; auto-updates on new tickets |
| Ticket Detail | Full ticket info + status update buttons + priority dropdown |
| Create Ticket | Form to register new support tickets |

## Architecture

**MVVM + Repository** with manual dependency injection via `AppContainer`.

```
UI (Compose Screens)
    ↓ observes StateFlow
ViewModels
    ↓ calls suspend functions
TicketRepository  ←  single source of truth
    ↓
MockTickets (current) / ApiService via Retrofit (future)
```

Key patterns:
- `StateFlow<List<Ticket>>` — drives reactive UI updates automatically
- `SharedFlow<TicketEvent>` — event bus for cross-screen side-effects (e.g., Snackbar on ticket creation)
- `ApiResult<T>` sealed class — consistent success/error handling across all operations
- `FeatureFlags.kt` — toggle features without touching multiple files

## Tech Stack

- Kotlin + Jetpack Compose
- MVVM + Repository pattern
- Retrofit + OkHttp (networking layer, ready for backend)
- Kotlin Coroutines + StateFlow / SharedFlow
- Manual DI via `AppContainer` (no Hilt — appropriate for PoC scope)
- Navigation Compose

## Running the Project

1. Open the `/app` folder in Android Studio Hedgehog or later
2. Sync Gradle
3. Run on an emulator or physical device (API 26+)

No backend or API keys required — the app runs entirely on mock data.

## Project Structure

```
/app        Android project (Jetpack Compose)
/contracts  OpenAPI 3.0 API spec (tickets-api.yaml + paths/ + schemas/)
/docs       Architecture decisions, event communication, feature flags
/video      Demo video links
```

## Feature Flags

Defined in `util/FeatureFlags.kt`. Set to `false` to disable without touching screen logic:

| Flag | Controls |
|---|---|
| `ENABLE_CREATE_TICKET` | FAB and Create Ticket screen access |
| `ENABLE_PRIORITY_UPDATE` | Priority dropdown in Ticket Detail |

## Integrating a Real Backend

When a backend is ready:
1. Update `BASE_URL` in `core/AppConstants.kt`
2. Uncomment the `ApiService` calls in `TicketRepository`
3. Remove the mock data delegation

All DTOs, endpoint interfaces, and error handling are already in place.
