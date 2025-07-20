# Copilot Instructions for Jetpack Compose Android Project

This document outlines key considerations when using Copilot for this Jetpack Compose project.

## General Guidelines

* **OpenJDK 21:** Ensure the project is set to use openjdk version "21.0.7" for compatibility with the latest Compose features.
* **Context is Key:** Provide surrounding `@Composable` functions, state declarations, and relevant data models for accurate suggestions.
* **Prioritize Kotlin Idioms:** When writing Kotlin, favor modern idiomatic expressions and features.
* **Prioritize Compose Idioms:** Favor declarative UI, unidirectional data flow, and the use of `State`, `MutableState`, `remember`, `rememberSaveable`, `ViewModel`, and `Flow`.
* **Modularity & Reusability:** Encourage small, focused, and reusable `@Composable` functions. Think components.
* **Performance Awareness:** Avoid unnecessary recompositions. Utilize `key`s in `LazyColumn`/`LazyRow` and `derivedStateOf` where appropriate.
* **Resource Management:** Be mindful of string, dimension, color, and drawable resources. Encourage referencing existing resources where appropriate.

### Kotlin Code

* **AndroidX Libraries:** Prefer AndroidX libraries over legacy support libraries.
* **Lifecycle Awareness:** Generate code that respects Android lifecycles (e.g., `ViewModel`, `LifecycleOwner`).
* **Coroutines/Flow:** For asynchronous operations, prioritize Kotlin Coroutines and Flow over older callback-based patterns.
* **Dependency Injection:** If using a DI framework (e.g., Hilt, Koin), ensure generated code integrates correctly.

### Composable Functions

* **State Management:**
    * For UI-specific state within a composable, use `remember { mutableStateOf(...) }`.
    * For state that needs to survive configuration changes, use `rememberSaveable { mutableStateOf(...) }`.
    * For screen-level UI state, use `ViewModel` and expose `StateFlow` or `LiveData` (converted to `State` with `collectAsState`/`observeAsState`).
    * Pass state *down* and events *up* (unidirectional data flow).
* **Modifiers:** Apply modifiers thoughtfully, often to the top-most layout composable. Chain them logically.
* **Material Design 3:** Adhere to Material Design 3 guidelines for consistent UI, including `MaterialTheme` for colors, typography, and shapes.
* **Layouts:** Prefer `Column`, `Row`, `Box`, `ConstraintLayout`, `LazyColumn`, `LazyRow`, and `LazyVerticalGrid` for structuring UI.
* **Animations:** Suggest using built-in Compose animation APIs like `animate*AsState`, `Transition`, and `AnimatedVisibility`.

### Data Flow & Architecture

* **Separation of Concerns:** Keep UI logic within composables and business logic in ViewModels or domain layers.
* **Immutability:** Prefer immutable data classes for UI state to ensure correctness and optimize recomposition.

### Build System (Gradle)

* **Dependencies:** Suggest up-to-date and stable versions of libraries.
* **Build Features:** Be aware of build features like `viewBinding`, `dataBinding`, and `compose`.

### Testing

* **Unit Tests:** Focus on isolated unit tests for business logic.
* **Instrumentation Tests:** Consider basic UI interactions and integration.
* **Composable Tests:** Generate tests using `ComposeTestRule` to verify UI behavior and appearance.
* **Semantics Tree:** Understand that Compose tests interact with the semantics tree.

---

**Note:** Always review Copilot's suggestions carefully and ensure they align with Android best practices and project architecture.