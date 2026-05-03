# Schedule Generator

This document describes the first schedule-generation engine introduced for the Android version of `cronograma-fiv-app`.

## Purpose

`ScheduleGenerator` converts a reusable `ReproductiveProtocol` into a concrete `Schedule` with dated `ScheduleEvent` instances.

The generator is intentionally part of the domain service layer. It does not depend on Jetpack Compose, Room, Android notifications or any UI-specific API.

## Inputs

The generator receives:

- `ReproductiveProtocol`: the protocol template;
- `ScheduleAnchor`: the type of reference date selected by the user;
- `anchorDate`: the actual date selected by the user;
- optional `farmName`;
- optional `responsibleName`.

## Supported anchors

### PROTOCOL_START

The selected date is treated directly as the protocol start date.

Example:

```text
Protocol start: 2026-01-10
D0: 2026-01-10
D8: 2026-01-18
D17: 2026-01-27
```

### EMBRYO_TRANSFER

The selected date is treated as the embryo transfer date. The generator looks for the transfer step in the protocol and subtracts its `dayOffset` to recover the protocol start date.

Example for the recipient protocol:

```text
Embryo transfer date: 2026-01-27
Transfer step offset: D17
Protocol start: 2026-01-10
```

### EXPECTED_BIRTH

The selected date is treated as the expected birth date. The generator looks for the expected birth step and subtracts its `dayOffset` to recover the schedule start date.

Example for pregnancy follow-up:

```text
Expected birth: 2026-12-31
Birth step offset: D280
Reference start: 2026-03-26
```

## Generated output

The output is a `Schedule` containing:

- generated schedule id;
- source protocol id;
- source protocol name;
- source protocol type;
- anchor used;
- anchor date;
- optional farm name;
- optional responsible name;
- generated dated events;
- creation date;
- update date.

Each generated `ScheduleEvent` contains:

- generated event id;
- source protocol step id;
- title;
- description;
- calculated date;
- original day offset;
- display order;
- completion status;
- optional notes.

## Test coverage

The initial tests cover:

- recipient schedule generated from protocol start date;
- recipient schedule generated from embryo transfer date;
- donor/PIVE schedule generated from protocol start date;
- pregnancy follow-up generated from expected birth date;
- cleanup of blank optional names.

Run locally with:

```bash
./gradlew :app:testDebugUnitTest
```

or, if the Gradle Wrapper is not available yet:

```bash
gradle :app:testDebugUnitTest
```

## Next steps

Future phases should:

- connect the generator to the UI;
- add editing support for generated events;
- persist generated schedules with Room;
- schedule reminders based on generated event dates;
- export generated schedules to text/PDF.
