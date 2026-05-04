package br.com.cronogramafiv.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.cronogramafiv.data.local.dao.ScheduleDao
import br.com.cronogramafiv.data.local.entity.ScheduleEntity
import br.com.cronogramafiv.data.local.entity.ScheduleEventEntity

@Database(
    entities = [
        ScheduleEntity::class,
        ScheduleEventEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        private const val DATABASE_NAME = "cronograma_fiv.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME,
                ).build().also { instance = it }
            }
        }
    }
}
