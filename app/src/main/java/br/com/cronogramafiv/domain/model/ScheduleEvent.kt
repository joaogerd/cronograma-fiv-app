package br.com.cronogramafiv.domain.model

import java.time.LocalDate

/**
 * A dated event generated from a protocol step.
 */
data class ScheduleEvent(
    val id: String,
    val protocolStepId: String,
    val title: String,
    val description: String? = null,
    val date: LocalDate,
    val dayOffset: Int,
    val order: Int,
    val isCompleted: Boolean = false,
    val notes: String? = null,
) {
    init {
        require(id.isNotBlank()) { "Schedule event id must not be blank." }
        require(protocolStepId.isNotBlank()) { "Protocol step id must not be blank." }
        require(title.isNotBlank()) { "Schedule event title must not be blank." }
        require(order >= 0) { "Schedule event order must be zero or positive." }
    }
}
