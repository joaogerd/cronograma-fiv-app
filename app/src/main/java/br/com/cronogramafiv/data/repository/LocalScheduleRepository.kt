package br.com.cronogramafiv.data.repository

import br.com.cronogramafiv.data.local.dao.ScheduleDao
import br.com.cronogramafiv.data.mapper.toDomain
import br.com.cronogramafiv.data.mapper.toEntity
import br.com.cronogramafiv.data.mapper.toEventEntities
import br.com.cronogramafiv.domain.model.Schedule
import br.com.cronogramafiv.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalScheduleRepository(
    private val scheduleDao: ScheduleDao,
) : ScheduleRepository {
    override fun observeSchedules(): Flow<List<Schedule>> =
        scheduleDao.observeSchedules().map { schedules ->
            schedules.map { it.toDomain() }
        }

    override suspend fun getScheduleById(id: String): Schedule? =
        scheduleDao.getScheduleById(id)?.toDomain()

    override suspend fun saveSchedule(schedule: Schedule) {
        scheduleDao.upsertScheduleWithEvents(
            schedule = schedule.toEntity(),
            events = schedule.toEventEntities(),
        )
    }

    override suspend fun deleteScheduleById(id: String) {
        scheduleDao.deleteScheduleById(id)
    }
}
