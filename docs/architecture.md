# Architecture & Technical Decisions

## 1. Architectural Pattern: MVVM + Repository

```
View (Compose UI)
      ↓ observes UiState
ViewModel
      ↓ orchestrates
Repository (single source of truth)
      ↓
MockData (PoC) → Retrofit (future backend)
```

**MVVM — one ViewModel per screen, not a global one.**
Each screen has its own lifecycle and state. A global ViewModel would couple unrelated screens and make the project harder to maintain.

**Repository pattern.**
The Repository is the only class that knows where data comes from — mock or real API. ViewModels and UI never care about the source. Switching to a real backend only requires changes inside the repository.

---

## 2. Package Structure & Responsibilities

| Package | Responsibility |
|---------|---------------|
| `data/model/` | Domain entities used across the app (`Ticket`, enums) |
| `data/remote/` | Retrofit interface and DTOs — prepared for future backend |
| `data/remote/model/` | DTOs with `@SerializedName` + mappers to domain models |
| `data/mock/` | Realistic Panini CR mock data — `mutableListOf` for in-memory mutation |
| `data/repository/` | Single source of truth — exposes `StateFlow` and `SharedFlow` |
| `ui/screens/` | One folder per screen, each with `Screen.kt` + `ViewModel.kt` + `UiState.kt` |
| `ui/components/` | Reusable Compose components (`TicketCard`) |
| `core/` | App-wide constants: `BASE_URL`, navigation routes |
| `util/` | `FeatureFlags.kt` |

---

## 3. Key Technical Decisions

### DTOs vs Domain Models
DTOs are kept separate from domain models with explicit mappers:
```kotlin
fun TicketDto.toDomain() = Ticket(
    id = id,
    title = title,
    priority = TicketPriority.valueOf(priority),
    ...
)
```
If the backend changes a field name, only the DTO and mapper change — the rest of the app stays untouched.

### ApiResult sealed class
Every repository operation returns `ApiResult<T>`:
```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}
```
Avoids unhandled exceptions. The ViewModel always processes an explicit result.

### UiState per screen
Every screen has a sealed `UiState` with three states:
```kotlin
sealed class TicketListUiState {
    object Loading : TicketListUiState()
    data class Success(val tickets: List<Ticket>) : TicketListUiState()
    data class Error(val message: String) : TicketListUiState()
}
```
Even with mock data, the pattern is ready for real async network calls without refactoring.

### Sorting responsibility
Tickets are always sorted `HIGH → MEDIUM → LOW` inside the Repository, not in the UI. Business logic does not belong in Compose composables.

### Manual DI — AppContainer, no Hilt
A single `AppContainer` object with `lazy` properties provides all dependencies:
```kotlin
object AppContainer {
    val ticketRepository: TicketRepository by lazy { TicketRepository() }
}
```
Hilt adds annotations, build plugins, and a learning curve that are disproportionate for a short-scope PoC. Any engineer can read `AppContainer` and immediately understand the dependency graph.

### No Room Database
The exam explicitly states no real backend is required. Room would add schema, migrations, and DAO boilerplate with no benefit for a PoC. The Repository is structured so Room can be added later without touching ViewModels or UI.

### Code language — English
All class names, variable names, function names, and comments are written in technical English, consistent with professional Android development standards.

---

## 4. Networking Layer

The full networking structure is in place even though no backend exists yet:

```
core/AppConstants.kt        ← BASE_URL, route constants
        ↓
data/remote/RetrofitClient.kt   ← OkHttpClient + Gson + logging interceptor
        ↓
data/remote/ApiService.kt       ← Retrofit interface with all endpoints
        ↓
data/repository/TicketRepository.kt  ← mock calls active, Retrofit calls commented
```

To connect to a real backend:
1. Set `BASE_URL` in `AppConstants.kt`
2. Uncomment Retrofit calls in `TicketRepository.kt`
3. No other files need to change

---

## 5. System Flow

```
App Launch
    ↓
LoginScreen
  [simulated — any non-empty email + password]
    ↓
TicketListScreen
  [LazyColumn sorted HIGH → MEDIUM → LOW]
    ↓
  ┌──────────────────────┬─────────────────────┐
  ↓                      ↓                     
TicketDetailScreen    CreateTicketScreen       
  · full ticket info    · form + validation    
  · update status       · on submit → event   
  · update priority       → list updates      
  · emits events                               
```

| Screen | Responsibility |
|--------|---------------|
| `LoginScreen` | Simulated auth. Validates non-empty fields. Navigates to list. |
| `TicketListScreen` | Displays tickets sorted by priority. Reacts to repository events automatically. |
| `TicketDetailScreen` | Full ticket info. Status and priority updates. Changes reflect in list immediately. |
| `CreateTicketScreen` | New ticket form. On submit emits event that updates the list without manual reload. |
