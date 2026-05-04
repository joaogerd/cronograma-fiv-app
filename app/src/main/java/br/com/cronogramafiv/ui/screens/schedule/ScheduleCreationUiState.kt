package br.com.cronogramafiv.ui.screens.schedule

import br.com.cronogramafiv.domain.model.BuiltInProtocols
import br.com.cronogramafiv.domain.model.ReproductiveProtocol
import br.com.cronogramafiv.domain.model.Schedule
import br.com.cronogramafiv.domain.model.ScheduleAnchor
import java.time.LocalDate

/**
 * Immutable state rendered by the schedule creation screen.
 */
data class ScheduleCreationUiState(
    val protocols: List<ReproductiveProtocol> = BuiltInProtocols.all,
    val anchors: List<ScheduleAnchor> = ScheduleAnchor.entries,
    val selectedProtocol: ReproductiveProtocol = protocols.first(),
    val selectedAnchor: ScheduleAnchor = ScheduleAnchor.PROTOCOL_START,
    val dateText: String = LocalDate.now().toString(),
    val farmName: String = "",
    val responsibleName: String = "",
    val generatedSchedule: Schedule? = null,
    val errorMessage: String? = null,
) {
    val hasGeneratedSchedule: Boolean
        get() = generatedSchedule != null
}

val ScheduleAnchor.label: String
    get() = when (this) {
        ScheduleAnchor.PROTOCOL_START -> "Início do protocolo"
        ScheduleAnchor.EMBRYO_TRANSFER -> "Transferência embrionária"
        ScheduleAnchor.EXPECTED_BIRTH -> "Nascimento previsto"
    }
