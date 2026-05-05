package br.com.cronogramafiv.ui.screens.history

import br.com.cronogramafiv.domain.model.BuiltInProtocols
import br.com.cronogramafiv.domain.model.Schedule
import br.com.cronogramafiv.domain.model.ScheduleAnchor
import br.com.cronogramafiv.domain.service.CurrentDateProvider
import br.com.cronogramafiv.domain.service.IdProvider
import br.com.cronogramafiv.domain.service.ScheduleGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ScheduleHistoryUiStateTest {
    @Test
    fun emptyStateReportsEmptyHistory() {
        val state = ScheduleHistoryUiState()

        assertTrue(state.isEmpty)
        assertFalse(state.hasNoSearchResults)
        assertEquals(emptyList<Schedule>(), state.filteredSchedules)
    }

    @Test
    fun filteredSchedulesMatchesFarmName() {
        val state = ScheduleHistoryUiState(
            schedules = listOf(
                sampleSchedule(farmName = "Fazenda Moquem"),
                sampleSchedule(farmName = "Fazenda Santa Clara"),
            ),
            searchQuery = "moquem",
        )

        assertEquals(1, state.filteredSchedules.size)
        assertEquals("Fazenda Moquem", state.filteredSchedules.first().farmName)
    }

    @Test
    fun filteredSchedulesMatchesProtocolName() {
        val state = ScheduleHistoryUiState(
            schedules = listOf(
                sampleSchedule(farmName = "Fazenda Moquem"),
            ),
            searchQuery = "receptora",
        )

        assertEquals(1, state.filteredSchedules.size)
    }

    @Test
    fun hasNoSearchResultsReportsWhenQueryDoesNotMatch() {
        val state = ScheduleHistoryUiState(
            schedules = listOf(
                sampleSchedule(farmName = "Fazenda Moquem"),
            ),
            searchQuery = "inexistente",
        )

        assertFalse(state.isEmpty)
        assertTrue(state.hasNoSearchResults)
        assertEquals(emptyList<Schedule>(), state.filteredSchedules)
    }

    private fun sampleSchedule(farmName: String): Schedule {
        val generator = ScheduleGenerator(
            idProvider = IncrementalIdProvider(),
            currentDateProvider = CurrentDateProvider { LocalDate.of(2026, 5, 4) },
        )

        return generator.generate(
            protocol = BuiltInProtocols.recipient,
            anchor = ScheduleAnchor.PROTOCOL_START,
            anchorDate = LocalDate.of(2026, 1, 10),
            farmName = farmName,
            responsibleName = "João",
        )
    }
}

private class IncrementalIdProvider : IdProvider {
    private var value = 0

    override fun nextId(): String {
        value += 1
        return "id-$value"
    }
}
