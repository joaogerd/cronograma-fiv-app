# Domain Model

This document describes the first domain layer introduced for the Android version of `cronograma-fiv-app`.

The goal of this layer is to represent reproductive protocols and schedules independently from UI, persistence and notification concerns.

## Core concepts

### ProtocolType

Represents the high-level category of a reproductive protocol:

- `RECIPIENT`
- `DONOR_PIVE`
- `PREGNANCY_FOLLOW_UP`
- `CUSTOM`

### ScheduleAnchor

Represents the kind of date used by the user as the reference for schedule generation:

- `PROTOCOL_START`
- `EMBRYO_TRANSFER`
- `EXPECTED_BIRTH`

### ProtocolStep

Represents one planned action inside a protocol.

Main fields:

- `id`
- `title`
- `description`
- `dayOffset`
- `order`
- `isRequired`

The `dayOffset` field is intentionally relative to the protocol start. The schedule generator will later translate this offset into a real calendar date.

### ReproductiveProtocol

Represents a reusable protocol template.

Main fields:

- `id`
- `name`
- `type`
- `description`
- `steps`
- `isBuiltIn`

The model validates that:

- protocol id is not blank;
- protocol name is not blank;
- each protocol has at least one step;
- step ids are unique within the same protocol.

### ScheduleEvent

Represents one dated event generated from a protocol step.

Main fields:

- `id`
- `protocolStepId`
- `title`
- `description`
- `date`
- `dayOffset`
- `order`
- `isCompleted`
- `notes`

### Schedule

Represents a concrete generated schedule.

Main fields:

- `id`
- `protocolId`
- `protocolName`
- `protocolType`
- `anchor`
- `anchorDate`
- `farmName`
- `responsibleName`
- `events`
- `createdAt`
- `updatedAt`

The model exposes computed properties:

- `orderedEvents`
- `startDate`
- `endDate`

## Built-in protocols

The first built-in protocol templates are defined in `BuiltInProtocols`:

### Recipient standard protocol

- D0: synchronization;
- D8: implant removal;
- D17: embryo transfer.

### Donor / PIVE standard protocol

- D9 / D-1: OPU;
- D10 / D0: FIV;
- D11 / D1: CIV;
- D13 / D3: feeding 50%;
- D15 / D5: feeding 80%;
- D17 / D7: embryo transfer or cryopreservation.

### Pregnancy follow-up standard protocol

- D0: embryo transfer;
- D1: transfer report;
- D30: pregnancy diagnosis;
- D60: fetal sexing;
- D280: expected birth.

## Next phase

The next phase should implement the schedule-generation engine:

```text
feat/schedule-generator
```

Expected class:

```text
app/src/main/java/br/com/cronogramafiv/domain/service/ScheduleGenerator.kt
```

The generator should convert a `ReproductiveProtocol` and a selected anchor date into a concrete `Schedule` with dated `ScheduleEvent` instances.
