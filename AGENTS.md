# AGENTS.md — HackerFeed

## Project Overview

Android app (Kotlin, Jetpack Compose, Material 3) displaying Hacker News stories with favourites, caching, and settings. Package: `com.vpk.hackerfeed`. Min SDK 24, target SDK 35.

## Architecture

```
app/src/main/java/com/vpk/hackerfeed/
├── components/       # Reusable Compose UI components
├── data/             # Repository implementations, API, cache, datasources
├── database/         # Room entities and DAOs
├── di/               # Manual DI (AppContainer, ViewModelFactory)
├── domain/           # Models, repository interfaces, use cases
├── presentation/     # ViewModels per feature
├── ui/theme/         # Color.kt, Theme.kt, Type.kt (Cyberpunk/Neon theme)
├── *Activity.kt      # Screen-level activities (News, Favourites, Settings, About, Cache, Legal)
└── HackerFeedApplication.kt
```

## Tooling

All tools managed via `mise.toml`. Run `mise install` after cloning.

- **Java**: Temurin 21
- **Android SDK**: Command-line tools v20.0 (platform 35, build-tools 35)
- **yq**: Required dependency for android-sdk plugin

## Build & Run Commands

| Command | Purpose |
|---------|---------|
| `mise run build` | Build debug APK |
| `mise run build:release` | Build release APK |
| `mise run build:bundle` | Build release AAB |
| `mise run clean` | Delete build outputs |
| `mise run rebuild` | Clean + build debug |
| `mise run test` | Run unit tests |
| `mise run test:connected` | Instrumentation tests on device |
| `mise run lint` | Android lint |
| `mise run check` | All checks (lint + tests) |
| `mise run install` | Install debug APK on device |
| `mise run run` | Install + launch app |
| `mise run sdk:licenses` | Accept SDK licenses |

## Key Conventions

- **No hardcoded colors** — all colors come from `MaterialTheme.colorScheme` or named constants in `Color.kt`
- **Theme**: Cyberpunk/Neon — Electric Cyan (`#00FFFF`) primary, Hot Magenta (`#FF00FF`) secondary, dark bg `#0A0A0F`, light bg `#E8E8EC`
- **Fonts**: Orbitron (headlines/titles) + JetBrains Mono (body/labels), bundled in `res/font/`
- **API-level guards**: `BlurEffect` and glassmorphism require API 31+ — always guard with `Build.VERSION.SDK_INT >= Build.VERSION_CODES.S`
- **DI**: Manual dependency injection via `AppContainer` — no Hilt/Dagger
- **Navigation**: Activity-based (no Compose Navigation)
- **State management**: `StateFlow` in ViewModels, collected via `collectAsStateWithLifecycle`

## Commit Convention

- One-liner conventional commit messages only
- Format: `type(scope): concise message`
- Types: `feat`, `fix`, `refactor`, `style`, `docs`, `test`, `chore`, `perf`
- Scope = affected area: `theme`, `about`, `news`, `favourites`, `settings`, `cache`, `legal`, `components`, `build`, `deps`
- Examples:
  - `feat(about): update screen theme`
  - `fix(components): guard blur effect for API < 31`
  - `style(theme): add neon glow to card borders`
  - `refactor(di): simplify ViewModelFactory`
  - `chore(build): add mise tasks for lint and test`

## Best Practices

- Run `mise run build` after every change to catch compile errors early
- Run `mise run test` before committing
- Use `Modifier` as the last parameter with default `Modifier` in all composables
- Keep components stateless — state lives in ViewModels
- Use `stringResource()` for all user-facing text, never hardcoded strings
- When adding new colors, define them in `Color.kt` and map through `Theme.kt` color schemes
- Preview composables with both `darkTheme = true` and `darkTheme = false`
- Prefer `MaterialTheme.typography.*` over inline `TextStyle` — the typography system uses Orbitron/JetBrains Mono
- Keep card styling consistent: semi-transparent surface, 1dp cyan `BorderStroke`, `RoundedCornerShape(12.dp)`
- Animations must target 60fps — avoid allocations in `drawBehind`/`Canvas` lambdas
- Test on API 24 emulator to verify graceful degradation of blur/glow effects
