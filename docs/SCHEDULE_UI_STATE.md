# Schedule UI State Architecture

This document describes the UI state refactor introduced for the schedule creation flow.

## Goal

The goal of this phase is to move screen logic out of the Compose UI and into a ViewModel-driven state layer.

This prepares the app for:

- local persistence with Room;
- schedule history;
- sharing/export;
- reminders;
- better testability;
- cleaner UI components.

## Main files

```text
app/src/main/java/br/com/cronogramafiv/ui/screens/schedule/ScheduleCreationUiState.kt
app/src/main/java/br/com/cronogramafiv/ui/screens/schedule/ScheduleCreationViewModel.kt
app/src/main/java/br/com/cronogramafiv/ui/screens/schedule/ScheduleCreationScreen.kt
```

## ScheduleCreationUiState

`ScheduleCreationUiState` is an immutable representation of everything the screen needs to render.

It contains:

- available protocols;
- available schedule anchors;
- selected protocol;
- selected anchor;
- reference date text;
- farm name;
- responsible name;
- generated schedule;
- error message.

The screen receives this state and renders it. It should not own business logic.

## ScheduleCreationViewModel

`ScheduleCreationViewModel` owns screen behavior.

Responsibilities:

- update selected protocol;
- update selected anchor;
- update text fields;
- validate date input;
- call `ScheduleGenerator`;
- expose the resulting `ScheduleCreationUiState`.

It exposes state through:

```kotlin
StateFlow<ScheduleCreationUiState>
```

## ScheduleCreationRoute

`ScheduleCreationRoute` connects the Android lifecycle/ViewModel layer to the pure Compose screen.

It collects `uiState` and passes callbacks to `ScheduleCreationScreen`.

## ScheduleCreationScreen

`ScheduleCreationScreen` is now a stateless-ish Composable.

It receives:

- `uiState`;
- callbacks for user interactions.

This makes the screen easier to preview, test and reuse.

## Test coverage

The ViewModel tests cover:

- initial state;
- schedule generation with valid date;
- error state with invalid date;
- clearing generated schedule when protocol changes;
- clearing generated schedule when anchor changes.

Run locally with:

```bash
./gradlew :app:testDebugUnitTest
```

To run the Compose UI smoke test, connect a device or start an emulator and run:

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Next phase

The next phase should add local persistence with Room:

```text
feat/local-storage
```

Expected work:

- add Room dependencies;
- create schedule entities;
- create event entities;
- create DAOs;
- add repository abstraction;
- persist generated schedules.
