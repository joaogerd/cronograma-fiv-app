package br.com.cronogramafiv.data.local

import androidx.room.TypeConverter
import br.com.cronogramafiv.domain.model.ProtocolType
import br.com.cronogramafiv.domain.model.ScheduleAnchor

class Converters {
    @TypeConverter
    fun protocolTypeToString(value: ProtocolType): String = value.name

    @TypeConverter
    fun stringToProtocolType(value: String): ProtocolType = ProtocolType.valueOf(value)

    @TypeConverter
    fun scheduleAnchorToString(value: ScheduleAnchor): String = value.name

    @TypeConverter
    fun stringToScheduleAnchor(value: String): ScheduleAnchor = ScheduleAnchor.valueOf(value)
}
