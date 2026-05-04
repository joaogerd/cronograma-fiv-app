package br.com.cronogramafiv.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ScheduleWithEvents(
    @Embedded val schedule: ScheduleEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "scheduleId",
    )
    val events: List<ScheduleEventEntity>,
)
