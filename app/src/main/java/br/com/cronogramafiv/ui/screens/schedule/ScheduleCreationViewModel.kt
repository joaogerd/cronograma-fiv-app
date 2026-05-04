package br.com.cronogramafiv.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cronogramafiv.domain.model.ReproductiveProtocol
import br.com.cronogramafiv.domain.model.ScheduleAnchor
import br.com.cronogramafiv.domain.repository.ScheduleRepository
import br.com.cronogramafiv.domain.service.ScheduleGenerator
import java.time.LocalDate
import java.time.format.DateTimeParseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScheduleCreationViewModel(
    private val generator: ScheduleGenerator = ScheduleGenerator(),
    private val scheduleRepository: ScheduleRepository? = null,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleCreationUiState())
    val uiState: StateFlow<ScheduleCreationUiState> = _uiState.asStateFlow()

    fun onProtocolSelected(protocol: ReproductiveProtocol) {
        _uiState.update {
            it.copy(
                selectedProtocol = protocol,
                generatedSchedule = null,
                errorMessage = null,
                successMessage = null,
            )
        }
    }

    fun onAnchorSelected(anchor: ScheduleAnchor) {
        _uiState.update {
            it.copy(
                selectedAnchor = anchor,
                generatedSchedule = null,
                errorMessage = null,
                successMessage = null,
            )
        }
    }

    fun onDateTextChanged(value: String) {
        _uiState.update {
            it.copy(
                dateText = value,
                generatedSchedule = null,
                errorMessage = null,
                successMessage = null,
            )
        }
    }

    fun onFarmNameChanged(value: String) {
        _uiState.update { it.copy(farmName = value, successMessage = null) }
    }

    fun onResponsibleNameChanged(value: String) {
        _uiState.update { it.copy(responsibleName = value, successMessage = null) }
    }

    fun generateSchedule() {
        val state = _uiState.value
        val parsedDate = parseDate(state.dateText)

        if (parsedDate == null) {
            _uiState.update {
                it.copy(
                    generatedSchedule = null,
                    errorMessage = "Data inválida. Use o formato AAAA-MM-DD.",
                    successMessage = null,
                )
            }
            return
        }

        runCatching {
            generator.generate(
                protocol = state.selectedProtocol,
                anchor = state.selectedAnchor,
                anchorDate = parsedDate,
                farmName = state.farmName,
                responsibleName = state.responsibleName,
            )
        }.onSuccess { schedule ->
            _uiState.update {
                it.copy(
                    generatedSchedule = schedule,
                    errorMessage = null,
                    successMessage = null,
                )
            }
        }.onFailure { throwable ->
            _uiState.update {
                it.copy(
                    generatedSchedule = null,
                    errorMessage = throwable.message ?: "Não foi possível gerar o cronograma.",
                    successMessage = null,
                )
            }
        }
    }

    fun saveGeneratedSchedule() {
        val repository = scheduleRepository
        val schedule = _uiState.value.generatedSchedule

        if (repository == null) {
            _uiState.update {
                it.copy(
                    errorMessage = "Repositório local indisponível.",
                    successMessage = null,
                )
            }
            return
        }

        if (schedule == null) {
            _uiState.update {
                it.copy(
                    errorMessage = "Gere um cronograma antes de salvar.",
                    successMessage = null,
                )
            }
            return
        }

        viewModelScope.launch {
            runCatching {
                repository.saveSchedule(schedule)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = "Cronograma salvo no dispositivo.",
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        errorMessage = throwable.message ?: "Não foi possível salvar o cronograma.",
                        successMessage = null,
                    )
                }
            }
        }
    }

    private fun parseDate(value: String): LocalDate? {
        return try {
            LocalDate.parse(value.trim())
        } catch (_: DateTimeParseException) {
            null
        }
    }
}
