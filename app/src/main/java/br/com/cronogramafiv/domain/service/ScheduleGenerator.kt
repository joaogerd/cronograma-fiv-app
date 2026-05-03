package br.com.cronogramafiv.domain.service

import br.com.cronogramafiv.domain.model.ProtocolStep
import br.com.cronogramafiv.domain.model.ReproductiveProtocol
import br.com.cronogramafiv.domain.model.Schedule
import br.com.cronogramafiv.domain.model.ScheduleAnchor
import br.com.cronogramafiv.domain.model.ScheduleEvent
import java.time.LocalDate
import java.util.UUID

/**
 * Generates concrete reproductive schedules from protocol templates.
 *
 * Protocol steps are stored relative to the protocol start date. This generator
 * converts a user-provided anchor date into a protocol start date and then
 * materializes each step as a dated schedule event.
 */
class ScheduleGenerator(
    private val idProvider: IdProvider = RandomIdProvider,
    private val currentDateProvider: CurrentDateProvider = SystemCurrentDateProvider,
) {
    fun generate(
        protocol: ReproductiveProtocol,
        anchor: ScheduleAnchor,
        anchorDate: LocalDate,
        farmName: String? = null,
        responsibleName: String? = null,
    ): Schedule {
        val protocolStartDate = resolveProtocolStartDate(
            protocol = protocol,
            anchor = anchor,
            anchorDate = anchorDate,
        )

        val events = protocol.orderedSteps.map { step ->
            step.toScheduleEvent(protocolStartDate)
        }

        val today = currentDateProvider.today()

        return Schedule(
            id = idProvider.nextId(),
            protocolId = protocol.id,
            protocolName = protocol.name,
            protocolType = protocol.type,
            anchor = anchor,
            anchorDate = anchorDate,
            farmName = farmName?.trimToNull(),
            responsibleName = responsibleName?.trimToNull(),
            events = events,
            createdAt = today,
            updatedAt = today,
        )
    }

    private fun resolveProtocolStartDate(
        protocol: ReproductiveProtocol,
        anchor: ScheduleAnchor,
        anchorDate: LocalDate,
    ): LocalDate {
        return when (anchor) {
            ScheduleAnchor.PROTOCOL_START -> anchorDate
            ScheduleAnchor.EMBRYO_TRANSFER -> anchorDate.minusDays(
                protocol.requiredDayOffsetForAnchor(anchor).toLong(),
            )
            ScheduleAnchor.EXPECTED_BIRTH -> anchorDate.minusDays(
                protocol.requiredDayOffsetForAnchor(anchor).toLong(),
            )
        }
    }

    private fun ReproductiveProtocol.requiredDayOffsetForAnchor(anchor: ScheduleAnchor): Int {
        val candidate = when (anchor) {
            ScheduleAnchor.PROTOCOL_START -> 0
            ScheduleAnchor.EMBRYO_TRANSFER -> findStepOffsetByTerms(
                containsAny = listOf(
                    "embryo transfer",
                    "transferencia",
                    "transferência",
                    "transfer",
                ),
                exactTokens = listOf("te"),
            )
            ScheduleAnchor.EXPECTED_BIRTH -> findStepOffsetByTerms(
                containsAny = listOf(
                    "expected birth",
                    "birth",
                    "nascimento",
                    "parto",
                ),
                exactTokens = emptyList(),
            )
        }

        return candidate ?: error(
            "Protocol '${name}' does not contain a step compatible with anchor '$anchor'.",
        )
    }

    private fun ReproductiveProtocol.findStepOffsetByTerms(
        containsAny: List<String>,
        exactTokens: List<String>,
    ): Int? {
        return orderedSteps.firstOrNull { step ->
            val searchableText = "${step.id} ${step.title} ${step.description.orEmpty()}".lowercase()
            val tokens = searchableText
                .split(Regex("[^a-z0-9áàâãéêíóôõúç]+"))
                .filter { it.isNotBlank() }

            containsAny.any { term -> searchableText.contains(term) } ||
                exactTokens.any { token -> token in tokens }
        }?.dayOffset
    }

    private fun ProtocolStep.toScheduleEvent(protocolStartDate: LocalDate): ScheduleEvent {
        return ScheduleEvent(
            id = idProvider.nextId(),
            protocolStepId = id,
            title = title,
            description = description,
            date = protocolStartDate.plusDays(dayOffset.toLong()),
            dayOffset = dayOffset,
            order = order,
        )
    }

    private fun String.trimToNull(): String? = trim().takeIf { it.isNotEmpty() }
}

fun interface IdProvider {
    fun nextId(): String
}

fun interface CurrentDateProvider {
    fun today(): LocalDate
}

object RandomIdProvider : IdProvider {
    override fun nextId(): String = UUID.randomUUID().toString()
}

object SystemCurrentDateProvider : CurrentDateProvider {
    override fun today(): LocalDate = LocalDate.now()
}
