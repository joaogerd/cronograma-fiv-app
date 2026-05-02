package br.com.cronogramafiv.domain.model

import java.time.LocalDate

/**
 * A concrete schedule created from a protocol and a selected reference date.
 */
data class Schedule(
    val id: String,
    val protocolId: String,
    val protocolName: String,
    val protocolType: ProtocolType,
    val anchor: ScheduleAnchor,
    val anchorDate: LocalDate,
    val farmName: String? = null,
    val responsibleName: String? = null,
    val events: List<ScheduleEvent>,
    val createdAt: LocalDate,
    val updatedAt: LocalDate = createdAt,
) {
    init {
        require(id.isNotBlank()) { "Schedule id must not be blank." }
        require(protocolId.isNotBlank()) { "Protocol id must not be blank." }
        require(protocolName.isNotBlank()) { "Protocol name must not be blank." }
        require(events.isNotEmpty()) { "Schedule must contain at least one event." }
        require(!updatedAt.isBefore(createdAt)) { "Schedule update date must not be before creation date." }
    }

    val orderedEvents: List<ScheduleEvent>
        get() = events.sortedWith(compareBy<ScheduleEvent> { it.date }.thenBy { it.order })

    val startDate: LocalDate
        get() = orderedEvents.first().date

    val endDate: LocalDate
        get() = orderedEvents.last().date
}
