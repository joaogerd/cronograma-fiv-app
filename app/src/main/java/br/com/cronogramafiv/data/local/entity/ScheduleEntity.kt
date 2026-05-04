package br.com.cronogramafiv.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.cronogramafiv.domain.model.ProtocolType
import br.com.cronogramafiv.domain.model.ScheduleAnchor

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey val id: String,
    val protocolId: String,
    val protocolName: String,
    val protocolType: ProtocolType,
    val anchor: ScheduleAnchor,
    val anchorDate: String,
    val farmName: String?,
    val responsibleName: String?,
    val createdAt: String,
    val updatedAt: String,
)
