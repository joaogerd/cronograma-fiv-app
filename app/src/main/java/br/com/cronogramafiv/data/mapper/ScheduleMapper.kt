package br.com.cronogramafiv.data.mapper

import br.com.cronogramafiv.data.local.entity.ScheduleEntity
import br.com.cronogramafiv.data.local.entity.ScheduleEventEntity
import br.com.cronogramafiv.data.local.entity.ScheduleWithEvents
import br.com.cronogramafiv.domain.model.Schedule
import br.com.cronogramafiv.domain.model.ScheduleEvent
import java.time.LocalDate

fun Schedule.toEntity(): ScheduleEntity = ScheduleEntity(
    id = id,
    protocolId = protocolId,
    protocolName = protocolName,
    protocolType = protocolType,
    anchor = anchor,
    anchorDate = anchorDate.toString(),
    farmName = farmName,
    responsibleName = responsibleName,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
)

fun ScheduleEvent.toEntity(scheduleId: String): ScheduleEventEntity = ScheduleEventEntity(
    id = id,
    scheduleId = scheduleId,
    protocolStepId = protocolStepId,
    title = title,
    description = description,
    date = date.toString(),
    dayOffset = dayOffset,
    eventOrder = order,
    isCompleted = isCompleted,
    notes = notes,
)

fun Schedule.toEventEntities(): List<ScheduleEventEntity> =
    orderedEvents.map { it.toEntity(scheduleId = id) }

fun ScheduleWithEvents.toDomain(): Schedule = Schedule(
    id = schedule.id,
    protocolId = schedule.protocolId,
    protocolName = schedule.protocolName,
    protocolType = schedule.protocolType,
    anchor = schedule.anchor,
    anchorDate = LocalDate.parse(schedule.anchorDate),
    farmName = schedule.farmName,
    responsibleName = schedule.responsibleName,
    events = events.map { it.toDomain() },
    createdAt = LocalDate.parse(schedule.createdAt),
    updatedAt = LocalDate.parse(schedule.updatedAt),
)

fun ScheduleEventEntity.toDomain(): ScheduleEvent = ScheduleEvent(
    id = id,
    protocolStepId = protocolStepId,
    title = title,
    description = description,
    date = LocalDate.parse(date),
    dayOffset = dayOffset,
    order = eventOrder,
    isCompleted = isCompleted,
    notes = notes,
)
