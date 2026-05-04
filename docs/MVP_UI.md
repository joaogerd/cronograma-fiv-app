# MVP Schedule Creation UI

This document describes the first functional Android UI for `cronograma-fiv-app`.

## Goal

The MVP UI connects the app entry point to the domain schedule generator.

The screen allows the user to:

- select a built-in protocol;
- select the schedule anchor;
- enter a reference date;
- optionally enter farm/property name;
- optionally enter responsible person;
- generate a schedule;
- view the generated events on screen.

## Current scope

This phase intentionally does not include:

- Room persistence;
- history;
- reminders;
- PDF export;
- share action;
- advanced date picker;
- custom protocol creation.

Those features should be added in later phases.

## Main screen

The main Android activity now renders:

```text
ScheduleCreationScreen
```

The screen is implemented in:

```text
app/src/main/java/br/com/cronogramafiv/ui/screens/schedule/ScheduleCreationScreen.kt
```

## Date input

The first MVP uses a plain text date field with ISO format:

```text
YYYY-MM-DD
```

Example:

```text
2026-01-10
```

This keeps the implementation simple and avoids introducing a date picker before the core flow is validated.

## Manual test flow

1. Open the app.
2. Select a protocol.
3. Select a calculation base.
4. Enter a date in `YYYY-MM-DD` format.
5. Optionally enter farm and responsible names.
6. Tap `Gerar cronograma`.
7. Confirm that dated events appear on screen.

## Local test commands

```bash
cd ~/cronograma-fiv-app
git checkout main
git pull origin main

git checkout feat/mvp-ui
git pull origin feat/mvp-ui

./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

To run instrumented UI tests, start an emulator or connect an Android device and run:

```bash
./gradlew :app:connectedDebugAndroidTest
```

If the Gradle Wrapper is not available locally, replace `./gradlew` with `gradle`.

## Next steps

Recommended next phases:

1. improve the MVP UI with a proper date picker;
2. add local persistence with Room;
3. add schedule history;
4. add sharing/export;
5. add reminders.
