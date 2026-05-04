# Product and UI Specification

This document defines the product direction, user experience and interface target for the Android version of `cronograma-fiv-app`.

It should be used as the main product reference for future implementation phases.

## 1. Product name

Development name:

```text
Cronograma FIV
```

Possible commercial names for future validation:

- CronoFIV;
- FIV Fácil;
- FIV Agenda.

For the current Android MVP, the app name remains `Cronograma FIV`.

## 2. Product concept

`Cronograma FIV` is an Android app for bovine reproductive schedule management, especially focused on FIV/PIVE routines.

The app converts a reference date into a complete reproductive schedule, reducing manual calculation, avoiding date errors and making communication with field teams easier.

Core flow:

```text
Choose protocol
→ choose calculation base
→ choose reference date
→ generate schedule
→ review events
→ save/share
```

## 3. Target audience

Primary users:

- veterinarians;
- field technicians;
- embryologists;
- reproduction teams;
- farms;
- FIV laboratories;
- reproduction centers.

Secondary users:

- farm assistants;
- farm managers;
- administrative staff;
- clients receiving generated schedules.

## 4. Value proposition

The app should provide a fast and reliable way to create reproductive schedules for field use.

Practical value:

- fewer spreadsheet errors;
- fewer manual date calculations;
- faster schedule creation;
- clearer communication with the farm team;
- easier tracking of critical reproductive events.

Commercial positioning:

```text
Less spreadsheet, fewer date errors and more control of reproductive routines.
```

## 5. UX principles

The app must be designed for field use.

Important assumptions:

- user may be in a hurry;
- user may be under sunlight;
- user may operate with one hand;
- internet may be unavailable;
- the app must work offline;
- interactions must be quick and clear.

Design principles:

- simple flow;
- large touch targets;
- readable dates;
- clear cards;
- minimal typing;
- no unnecessary administrative complexity;
- no backend dependency for MVP.

## 6. Main navigation

The target UI should use bottom navigation with three main tabs:

```text
Create | History | Protocols
```

A future version may add:

```text
Settings
```

For the MVP, three tabs are enough.

## 7. Create Schedule screen

Purpose:

Create a reproductive schedule in less than 30 seconds.

Target structure:

```text
Header: Create Schedule
Intro card
Protocol selector
Calculation base selector
Reference date picker
Farm/property field
Responsible field
Primary action button: Generate Schedule
```

Fields:

### Protocol

Initial options:

- Recipient - Standard protocol;
- Donor / PIVE - Standard protocol;
- Pregnancy follow-up - Standard protocol.

Future option:

- Create custom protocol.

### Calculation base

Initial options:

- Protocol start;
- Embryo transfer;
- Expected birth.

The app should only show compatible anchors for each selected protocol in future iterations.

### Reference date

Current MVP uses a text field in `YYYY-MM-DD` format.

Target UX should use a visual DatePicker and display dates as:

```text
dd/MM/yyyy
```

### Farm/property

Optional field.

### Responsible

Optional field.

### Main button

Text:

```text
Generate schedule
```

Portuguese UI text:

```text
GERAR CRONOGRAMA
```

The button should be large, green and nearly full width.

## 8. Schedule Result screen

Purpose:

Show the generated schedule clearly and allow the user to save or share it.

Target structure:

```text
Header: Schedule: <Farm name>
Summary card
Event cards
Actions: Save | Share
```

Summary card should show:

- protocol name;
- schedule period;
- farm/property when available;
- responsible person when available.

Each event card should show:

- date block;
- event title;
- event description;
- completion checkbox;
- future notes/action area.

Recommended date block format:

```text
10
JAN
```

The date must be visually prominent because it is the most important information in field use.

## 9. History screen

Purpose:

Allow users to find and reopen saved schedules.

Target structure:

```text
Header: History
Search field
Schedule cards
```

Search placeholder:

```text
Search farm/protocol...
```

Portuguese UI text:

```text
Buscar fazenda/protocolo...
```

Each history card should show:

- farm/property;
- protocol name;
- start date;
- number of events/steps.

On tap, the user should open the schedule detail screen.

Future actions:

- view details;
- share;
- duplicate;
- edit;
- delete.

## 10. Protocols screen

Purpose:

Show available protocols and later allow custom protocol management.

Initial scope:

- show built-in protocols;
- show number of steps;
- identify native app protocols.

Future scope:

- create custom protocol;
- edit custom protocol;
- duplicate built-in protocol;
- reorder steps;
- add/remove steps.

## 11. Visual style

The visual direction should follow the provided mockup.

Main characteristics:

- green top app bar;
- white or light neutral background;
- rounded cards;
- subtle borders;
- large main action button;
- bottom navigation with green highlight;
- date cards with strong green emphasis;
- simple Material 3 components.

Suggested palette:

```text
Primary green: #0B7A2A
Current green: #2E7D32
Soft green:    #DDF1DD
Surface:       #FFFFFF
Background:    #F7F7F7
Text:          #1B1F1A
Muted text:    #6B7568
```

## 12. Development roadmap

The approved roadmap from this point is:

### Phase 5 - MVP UI basic flow

Branch:

```text
feat/mvp-ui
```

Goal:

- first functional schedule creation UI;
- protocol selector;
- anchor selector;
- date input;
- schedule result cards.

Status: current phase.

### Phase 6 - UI state and ViewModel

Branch:

```text
feat/schedule-ui-state
```

Goal:

- add `ScheduleCreationViewModel`;
- add `ScheduleCreationUiState`;
- remove business/state logic from Composables;
- prepare UI for persistence;
- optionally introduce DatePicker if feasible in this phase.

### Phase 7 - Local persistence

Branch:

```text
feat/local-storage
```

Goal:

- add Room;
- persist generated schedules;
- persist events;
- map domain models to database entities.

### Phase 8 - History

Branch:

```text
feat/schedule-history
```

Goal:

- add history tab;
- list saved schedules;
- search by farm/protocol;
- open saved schedule details.

### Phase 9 - Sharing and export

Branch:

```text
feat/share-export
```

Goal:

- share schedule as text;
- later generate PDF;
- prepare printable client-facing output.

### Phase 10 - Reminders and notifications

Branch:

```text
feat/reminders
```

Goal:

- schedule local reminders;
- notify user before critical events;
- allow basic reminder configuration.

## 13. Current PR alignment

The current MVP UI is not expected to be final visual polish.

It should prove that:

- Android UI opens correctly;
- user can select a protocol;
- user can select a calculation base;
- user can input a date;
- app calls the real schedule generator;
- generated events are displayed.

Visual and architectural refinement will happen in Phase 6.
