package br.com.cronogramafiv.domain.model

/**
 * A single planned action inside a reproductive protocol.
 *
 * @property id Stable identifier used by the UI and persistence layers.
 * @property title Short name shown in lists and schedule cards.
 * @property description Optional operational detail for field use.
 * @property dayOffset Number of days relative to the protocol start date.
 * @property order Display order when two or more steps share the same date.
 * @property isRequired Whether the step is part of the minimum protocol definition.
 */
data class ProtocolStep(
    val id: String,
    val title: String,
    val description: String? = null,
    val dayOffset: Int,
    val order: Int,
    val isRequired: Boolean = true,
) {
    init {
        require(id.isNotBlank()) { "Protocol step id must not be blank." }
        require(title.isNotBlank()) { "Protocol step title must not be blank." }
        require(order >= 0) { "Protocol step order must be zero or positive." }
    }
}
