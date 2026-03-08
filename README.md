# GameBrowser

An Android case study app for browsing video games using the [RAWG API](https://rawg.io/apidocs). Built with a modern, production-oriented tech stack following Clean Architecture principles.

---

## Features

- Paginated list of games with infinite scroll
- Shimmer loading placeholders
- Game detail screen with description, rating, Metacritic score, and release date
- Room cache for game details — detail screen loads instantly on repeat visits
- Structured error handling with user-friendly messages
- Full offline error feedback (no internet, server error, unknown)

---

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose |
| Navigation | Navigation Compose (with slide animations) |
| Architecture | Clean Architecture + MVVM |
| Dependency Injection | Koin |
| Networking | Ktor (OkHttp engine) |
| Serialization | kotlinx.serialization |
| Local Cache | Room (game detail only) |
| Pagination | Paging 3 |
| Image Loading | Coil |
| Testing | JUnit4, MockK, kotlinx-coroutines-test |

---

## Architecture

The project is split into 6 Gradle modules enforcing strict layer separation:

```
app                  → Application entry point, Koin setup, navigation
├── domain           → Models, repository interfaces, use cases (pure JVM, no Android deps)
├── data             → Repository impl, Ktor API, Room DB, PagingSource
├── shared           → Design system: theme, spacing, reusable components, ViewModelBase
├── feature_list     → Games list screen + ViewModel
└── feature_detail   → Game detail screen + ViewModel
```

**Dependency rule:** outer layers depend inward. `domain` has zero Android dependencies.

```
feature_list / feature_detail
        ↓
      shared
        ↓
      domain
        ↑
       data
```

### Data flow

```
Screen → ViewModel → UseCase → Repository → (Ktor API / Room DB)
```

- **List**: `PagingSource` fetches from network only — always fresh
- **Detail**: checks Room cache first, falls back to network on miss, persists result

---

## Module Details

### `domain`
Pure Kotlin/JVM module. Contains:
- `Game`, `GameDetail` models
- `GameRepository` interface
- `GetGamesUseCase`, `GetGameDetailUseCase`
- `AppError` sealed class (`NoInternet`, `ServerError`, `Unknown`)

### `data`
- Ktor `HttpClient` with `ContentNegotiation`, `Logging`, `HttpResponseValidator`
- `GamesPagingSource` — Paging 3 source hitting RAWG `/games`
- `GameRepositoryImpl` — detail cache-first strategy with Room
- `safeApiCall` / `safePagingLoad` — generic error handling wrappers
- Room `GameDatabase` with `GameDetailDao`

### `shared`
- `GameBrowserTheme` with dark color scheme
- `MaterialTheme.spacing` extension — design token system for consistent spacing
- `ViewModelBase` — base class for all ViewModels
- Components: `RatingBadge`, `LoadingIndicator`, `ErrorState`
- `Modifier.shimmerEffect()` — animated shimmer for loading states
- `Throwable.toUserMessage()` — maps `AppError` to localized string resources

### `feature_list`
- `GamesListViewModel` — exposes `Flow<PagingData<Game>>`
- `GamesListScreen` — `LazyColumn` with `LazyPagingItems`, shimmer on initial load, append error footer

### `feature_detail`
- `GameDetailViewModel` — `StateFlow<GameDetailUiState>` (Loading / Success / Error)
- `GameDetailScreen` — hero image, rating badge, Metacritic score, release date, description

---

## Setup

### 1. Get a RAWG API key
Register at [rawg.io](https://rawg.io/apidocs) and copy your API key.

### 2. Configure local.properties
Create a `local.properties` file in the project root (already gitignored):

```properties
sdk.dir=/path/to/your/android/sdk
RAWG_API_KEY=your_api_key_here
```

A `local.properties.example` is included as a template.

### 3. Build & run
Open in Android Studio, sync Gradle, and run the `app` module.

**Requirements:**
- Android Studio Hedgehog or newer
- JDK 17+ (use Android Studio's bundled JBR)
- Android SDK 28+

---

## Testing

Unit tests live in `domain/src/test`:

```bash
./gradlew :domain:test
```

- `GetGamesUseCaseTest` — verifies delegation to repository
- `GetGameDetailUseCaseTest` — verifies success/failure paths and correct ID forwarding

---

## Project Structure

```
gamebrowser/
├── app/
│   ├── navigation/AppNavGraph.kt
│   ├── GameBrowserApp.kt
│   └── MainActivity.kt
├── domain/
│   ├── model/         Game.kt, GameDetail.kt
│   ├── error/         AppError.kt
│   ├── repository/    GameRepository.kt
│   └── usecase/       GetGamesUseCase.kt, GetGameDetailUseCase.kt
├── data/
│   ├── remote/        RawgApiService.kt, GamesPagingSource.kt, DTOs, mappers
│   ├── local/         GameDatabase.kt, GameDetailDao.kt, entity, mappers
│   ├── repository/    GameRepositoryImpl.kt
│   ├── di/            DataModule.kt
│   └── util/          ApiCallExt.kt
├── shared/
│   ├── base/          ViewModelBase.kt
│   ├── components/    RatingBadge.kt, LoadingIndicator.kt, ErrorState.kt, ShimmerEffect.kt
│   ├── error/         ThrowableExt.kt
│   └── theme/         Color.kt, Spacing.kt, Theme.kt
├── feature_list/
│   ├── GamesListScreen.kt
│   ├── GamesListViewModel.kt
│   └── di/FeatureListModule.kt
└── feature_detail/
    ├── GameDetailScreen.kt
    ├── GameDetailViewModel.kt
    ├── GameDetailUiState.kt
    └── di/FeatureDetailModule.kt
```
