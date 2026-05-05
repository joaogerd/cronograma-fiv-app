# Schedule History

This document describes the first schedule history screen for the Android version of `cronograma-fiv-app`.

## Goal

The goal of this phase is to allow the user to see schedules previously saved in the local Room database.

This completes the first practical loop:

```text
Create schedule -> Save schedule -> View in history
```

## Navigation

The app now uses a simple bottom navigation shell with two tabs:

```text
Criar | Histórico
```

Main files:

```text
app/src/main/java/br/com/cronogramafiv/ui/navigation/MainTab.kt
app/src/main/java/br/com/cronogramafiv/ui/navigation/CronogramaFivApp.kt
```

The `Protocolos` tab described in the product specification will be added later.

## History screen

Main files:

```text
app/src/main/java/br/com/cronogramafiv/ui/screens/history/ScheduleHistoryScreen.kt
app/src/main/java/br/com/cronogramafiv/ui/screens/history/ScheduleHistoryUiState.kt
app/src/main/java/br/com/cronogramafiv/ui/screens/history/ScheduleHistoryViewModel.kt
app/src/main/java/br/com/cronogramafiv/ui/screens/history/ScheduleHistoryViewModelFactory.kt
```

## Features

The first History screen supports:

- observing schedules saved in Room;
- displaying saved schedules as cards;
- showing farm/property name;
- showing protocol name;
- showing schedule period;
- showing number of events;
- searching by farm, protocol or responsible name;
- empty state when no schedule has been saved;
- no-results state when search does not match.

## Not included in this phase

This phase does not include:

- schedule detail screen;
- deleting saved schedules from the UI;
- editing saved schedules;
- duplicating schedules;
- sharing/exporting schedules;
- reminders.

Those features should be added in later phases.

## Manual test flow

1. Open the app.
2. Stay on `Criar`.
3. Generate a schedule.
4. Tap `Salvar cronograma`.
5. Open the `Histórico` tab.
6. Confirm that the saved schedule appears.
7. Use the search field to filter by farm/protocol.

## Local commands

```bash
cd /media/extra/wrk/dev/cronograma-fiv-app

git checkout main
git pull origin main

git checkout feat/schedule-history
git pull origin feat/schedule-history

./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

Instrumented UI test:

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Next phase

Recommended next phase:

```text
feat/share-export
```

Goal:

- share generated/saved schedules as text;
- prepare future PDF export.
