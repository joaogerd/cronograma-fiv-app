package br.com.cronogramafiv

import android.app.Application
import br.com.cronogramafiv.data.local.AppDatabase
import br.com.cronogramafiv.data.repository.LocalScheduleRepository
import br.com.cronogramafiv.domain.repository.ScheduleRepository

class CronogramaFivApplication : Application() {
    val scheduleRepository: ScheduleRepository by lazy {
        LocalScheduleRepository(
            scheduleDao = AppDatabase.getInstance(this).scheduleDao(),
        )
    }
}
