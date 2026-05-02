# Android Build Instructions

This document describes the initial Android build setup for `cronograma-fiv-app`.

## Current Android status

The Android project is currently in bootstrap stage. It includes:

- Gradle Kotlin DSL configuration;
- Android application module under `app/`;
- Kotlin source structure;
- Jetpack Compose setup;
- minimal `MainActivity`;
- basic Material 3 theme;
- placeholder screen identifying the Android base.

No production feature has been implemented yet. Protocol modeling, schedule generation, persistence and reminders will be added in later phases.

## Requirements

- Android Studio with support for recent Android Gradle Plugin versions;
- JDK 17;
- Android SDK Platform 36;
- Gradle 9.3.1 or compatible wrapper when added.

## Suggested local commands

After opening the project in Android Studio or adding a Gradle wrapper, run:

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
```

At this stage, the repository does not yet include the Gradle wrapper binaries. They should be generated locally with the Gradle version required by the Android Gradle Plugin before the first release-oriented CI setup.

## Next implementation phase

The next phase should add the domain layer:

```text
feat/domain-models
```

Expected modules:

```text
app/src/main/java/br/com/cronogramafiv/domain/model/Protocol.kt
app/src/main/java/br/com/cronogramafiv/domain/model/ProtocolStep.kt
app/src/main/java/br/com/cronogramafiv/domain/model/Schedule.kt
app/src/main/java/br/com/cronogramafiv/domain/model/ScheduleEvent.kt
```

The goal is to define protocols and events independently from the UI and persistence layers.
