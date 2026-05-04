package br.com.cronogramafiv.ui.screens.history

import br.com.cronogramafiv.domain.model.Schedule

data class ScheduleHistoryUiState(
    val schedules: List<Schedule> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null,
) {
    val filteredSchedules: List<Schedule>
        get() {
            val query = searchQuery.trim().lowercase()
            if (query.isBlank()) return schedules

            return schedules.filter { schedule ->
                schedule.protocolName.lowercase().contains(query) ||
                    schedule.farmName.orEmpty().lowercase().contains(query) ||
                    schedule.responsibleName.orEmpty().lowercase().contains(query)
            }
        }

    val isEmpty: Boolean
        get() = schedules.isEmpty()

    val hasNoSearchResults: Boolean
        get() = schedules.isNotEmpty() && filteredSchedules.isEmpty()
}
