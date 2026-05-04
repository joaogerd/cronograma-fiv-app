package br.com.cronogramafiv.domain.repository

import br.com.cronogramafiv.domain.model.Schedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun observeSchedules(): Flow<List<Schedule>>

    suspend fun getScheduleById(id: String): Schedule?

    suspend fun saveSchedule(schedule: Schedule)

    suspend fun deleteScheduleById(id: String)
}
