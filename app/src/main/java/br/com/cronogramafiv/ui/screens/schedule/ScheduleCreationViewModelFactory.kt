package br.com.cronogramafiv.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cronogramafiv.domain.repository.ScheduleRepository
import br.com.cronogramafiv.domain.service.ScheduleGenerator

class ScheduleCreationViewModelFactory(
    private val scheduleRepository: ScheduleRepository,
    private val generator: ScheduleGenerator = ScheduleGenerator(),
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleCreationViewModel::class.java)) {
            return ScheduleCreationViewModel(
                generator = generator,
                scheduleRepository = scheduleRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
