package br.com.cronogramafiv.domain.model

/**
 * Template used to generate reproductive schedules.
 *
 * The protocol itself does not hold dates. Dates are produced later by applying
 * each step offset to a selected anchor date.
 */
data class ReproductiveProtocol(
    val id: String,
    val name: String,
    val type: ProtocolType,
    val description: String? = null,
    val steps: List<ProtocolStep>,
    val isBuiltIn: Boolean = false,
) {
    init {
        require(id.isNotBlank()) { "Protocol id must not be blank." }
        require(name.isNotBlank()) { "Protocol name must not be blank." }
        require(steps.isNotEmpty()) { "Protocol must contain at least one step." }
        require(steps.map { it.id }.distinct().size == steps.size) {
            "Protocol step ids must be unique within the same protocol."
        }
    }

    val orderedSteps: List<ProtocolStep>
        get() = steps.sortedWith(compareBy<ProtocolStep> { it.dayOffset }.thenBy { it.order })
}
