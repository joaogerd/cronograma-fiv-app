# Local Storage

This document describes the first local persistence layer for the Android version of `cronograma-fiv-app`.

## Goal

The goal of this phase is to persist generated schedules and their events locally on the device.

This prepares the app for the next phase: schedule history.

## Technology

The persistence layer uses Room.

Main dependencies:

- `androidx.room:room-runtime`
- `androidx.room:room-ktx`
- `androidx.room:room-compiler` through KSP

## Database

Database file:

```text
cronograma_fiv.db
```

Main class:

```text
app/src/main/java/br/com/cronogramafiv/data/local/AppDatabase.kt
```

## Tables

### schedules

Entity:

```text
ScheduleEntity
```

Stores schedule-level metadata:

- id;
- protocol id;
- protocol name;
- protocol type;
- anchor;
- anchor date;
- farm name;
- responsible name;
- created date;
- updated date.

### schedule_events

Entity:

```text
ScheduleEventEntity
```

Stores events linked to one schedule:

- id;
- schedule id;
- protocol step id;
- title;
- description;
- date;
- day offset;
- display order;
- completion status;
- notes.

The table has a foreign key to `schedules` with cascade delete.

## DAO

Main DAO:

```text
ScheduleDao
```

Supported operations:

- insert/update schedule;
- replace events for a schedule;
- save schedule with events;
- get schedule by id;
- observe all schedules;
- delete schedule by id.

## Repository

Domain contract:

```text
ScheduleRepository
```

Local implementation:

```text
LocalScheduleRepository
```

The UI layer depends on the repository contract, not directly on Room.

## Mapping

Mappers are defined in:

```text
app/src/main/java/br/com/cronogramafiv/data/mapper/ScheduleMapper.kt
```

They convert:

```text
Schedule -> ScheduleEntity
ScheduleEvent -> ScheduleEventEntity
ScheduleWithEvents -> Schedule
```

## Application container

The app uses a lightweight application container:

```text
CronogramaFivApplication
```

It creates and exposes:

```text
ScheduleRepository
```

This keeps the current architecture simple without introducing a dependency injection framework too early.

## Current UI integration

After a schedule is generated, the screen now shows:

```text
Salvar cronograma
```

When tapped, the ViewModel saves the generated schedule using `ScheduleRepository`.

A success message is displayed after saving:

```text
Cronograma salvo no dispositivo.
```

## Not included in this phase

This phase does not include:

- history screen;
- schedule detail screen;
- search;
- editing saved schedules;
- export/share;
- reminders.

Those features should be implemented in later phases.

## Local test commands

```bash
cd /media/extra/wrk/dev/cronograma-fiv-app

git checkout main
git pull origin main

git checkout feat/local-storage
git pull origin feat/local-storage

./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

Manual test:

1. open the app;
2. generate a schedule;
3. tap `Salvar cronograma`;
4. confirm that the success message appears.

The next phase should add the history UI to read persisted schedules.
