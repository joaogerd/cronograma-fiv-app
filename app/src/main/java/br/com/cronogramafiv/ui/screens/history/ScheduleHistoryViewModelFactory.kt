package br.com.cronogramafiv.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cronogramafiv.domain.repository.ScheduleRepository

class ScheduleHistoryViewModelFactory(
    private val scheduleRepository: ScheduleRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleHistoryViewModel::class.java)) {
            return ScheduleHistoryViewModel(
                scheduleRepository = scheduleRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
