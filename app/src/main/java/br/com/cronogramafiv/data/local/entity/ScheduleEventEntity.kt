package br.com.cronogramafiv.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule_events",
    foreignKeys = [
        ForeignKey(
            entity = ScheduleEntity::class,
            parentColumns = ["id"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["scheduleId"])],
)
data class ScheduleEventEntity(
    @PrimaryKey val id: String,
    val scheduleId: String,
    val protocolStepId: String,
    val title: String,
    val description: String?,
    val date: String,
    val dayOffset: Int,
    val eventOrder: Int,
    val isCompleted: Boolean,
    val notes: String?,
)
