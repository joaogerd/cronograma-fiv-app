package br.com.cronogramafiv.ui.screens.schedule

import br.com.cronogramafiv.domain.model.BuiltInProtocols
import br.com.cronogramafiv.domain.model.ScheduleAnchor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ScheduleCreationViewModelTest {
    @Test
    fun initialStateContainsDefaultProtocolAnchorAndDate() {
        val viewModel = ScheduleCreationViewModel()
        val state = viewModel.uiState.value

        assertEquals(BuiltInProtocols.all, state.protocols)
        assertEquals(BuiltInProtocols.all.first(), state.selectedProtocol)
        assertEquals(ScheduleAnchor.PROTOCOL_START, state.selectedAnchor)
        assertNotNull(state.dateText)
        assertNull(state.generatedSchedule)
        assertNull(state.errorMessage)
    }

    @Test
    fun generateScheduleCreatesScheduleForValidDate() {
        val viewModel = ScheduleCreationViewModel()

        viewModel.onDateTextChanged("2026-01-10")
        viewModel.onFarmNameChanged("Fazenda Moquem")
        viewModel.onResponsibleNameChanged("João")
        viewModel.generateSchedule()

        val state = viewModel.uiState.value

        assertNull(state.errorMessage)
        assertNotNull(state.generatedSchedule)
        assertEquals("Fazenda Moquem", state.generatedSchedule?.farmName)
        assertEquals("João", state.generatedSchedule?.responsibleName)
        assertEquals("Receptora - Protocolo padrão", state.generatedSchedule?.protocolName)
        assertEquals(3, state.generatedSchedule?.events?.size)
    }

    @Test
    fun generateScheduleShowsErrorForInvalidDate() {
        val viewModel = ScheduleCreationViewModel()

        viewModel.onDateTextChanged("10/01/2026")
        viewModel.generateSchedule()

        val state = viewModel.uiState.value

        assertNull(state.generatedSchedule)
        assertEquals("Data inválida. Use o formato AAAA-MM-DD.", state.errorMessage)
    }

    @Test
    fun changingProtocolClearsGeneratedScheduleAndError() {
        val viewModel = ScheduleCreationViewModel()

        viewModel.onDateTextChanged("2026-01-10")
        viewModel.generateSchedule()
        assertNotNull(viewModel.uiState.value.generatedSchedule)

        viewModel.onProtocolSelected(BuiltInProtocols.donorPive)

        val state = viewModel.uiState.value

        assertEquals(BuiltInProtocols.donorPive, state.selectedProtocol)
        assertNull(state.generatedSchedule)
        assertNull(state.errorMessage)
    }

    @Test
    fun changingAnchorClearsGeneratedScheduleAndError() {
        val viewModel = ScheduleCreationViewModel()

        viewModel.onDateTextChanged("2026-01-10")
        viewModel.generateSchedule()
        assertNotNull(viewModel.uiState.value.generatedSchedule)

        viewModel.onAnchorSelected(ScheduleAnchor.EMBRYO_TRANSFER)

        val state = viewModel.uiState.value

        assertEquals(ScheduleAnchor.EMBRYO_TRANSFER, state.selectedAnchor)
        assertNull(state.generatedSchedule)
        assertNull(state.errorMessage)
    }
}
