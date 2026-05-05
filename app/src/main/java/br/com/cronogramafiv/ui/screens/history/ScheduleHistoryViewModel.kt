package br.com.cronogramafiv.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cronogramafiv.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ScheduleHistoryViewModel(
    private val scheduleRepository: ScheduleRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleHistoryUiState())
    val uiState: StateFlow<ScheduleHistoryUiState> = _uiState.asStateFlow()

    init {
        observeSchedules()
    }

    fun onSearchQueryChanged(value: String) {
        _uiState.update { it.copy(searchQuery = value) }
    }

    private fun observeSchedules() {
        scheduleRepository.observeSchedules()
            .onEach { schedules ->
                _uiState.update {
                    it.copy(
                        schedules = schedules,
                        errorMessage = null,
                    )
                }
            }
            .catch { throwable ->
                _uiState.update {
                    it.copy(
                        errorMessage = throwable.message ?: "Não foi possível carregar o histórico.",
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
