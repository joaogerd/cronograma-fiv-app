package br.com.cronogramafiv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.com.cronogramafiv.data.local.entity.ScheduleEntity
import br.com.cronogramafiv.data.local.entity.ScheduleEventEntity
import br.com.cronogramafiv.data.local.entity.ScheduleWithEvents
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<ScheduleEventEntity>)

    @Query("DELETE FROM schedule_events WHERE scheduleId = :scheduleId")
    suspend fun deleteEventsForSchedule(scheduleId: String)

    @Transaction
    suspend fun upsertScheduleWithEvents(
        schedule: ScheduleEntity,
        events: List<ScheduleEventEntity>,
    ) {
        insertSchedule(schedule)
        deleteEventsForSchedule(schedule.id)
        insertEvents(events)
    }

    @Transaction
    @Query("SELECT * FROM schedules WHERE id = :id")
    suspend fun getScheduleById(id: String): ScheduleWithEvents?

    @Transaction
    @Query("SELECT * FROM schedules ORDER BY createdAt DESC")
    fun observeSchedules(): Flow<List<ScheduleWithEvents>>

    @Query("DELETE FROM schedules WHERE id = :id")
    suspend fun deleteScheduleById(id: String)
}
