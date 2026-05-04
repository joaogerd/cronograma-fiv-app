package br.com.cronogramafiv.data.mapper

import br.com.cronogramafiv.data.local.entity.ScheduleWithEvents
import br.com.cronogramafiv.domain.model.BuiltInProtocols
import br.com.cronogramafiv.domain.model.ScheduleAnchor
import br.com.cronogramafiv.domain.service.CurrentDateProvider
import br.com.cronogramafiv.domain.service.IdProvider
import br.com.cronogramafiv.domain.service.ScheduleGenerator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ScheduleMapperTest {
    @Test
    fun scheduleMapsToEntitiesAndBackToDomain() {
        val generator = ScheduleGenerator(
            idProvider = IncrementalIdProvider(),
            currentDateProvider = CurrentDateProvider { LocalDate.of(2026, 5, 4) },
        )
        val schedule = generator.generate(
            protocol = BuiltInProtocols.recipient,
            anchor = ScheduleAnchor.PROTOCOL_START,
            anchorDate = LocalDate.of(2026, 1, 10),
            farmName = "Fazenda Moquem",
            responsibleName = "João",
        )

        val scheduleEntity = schedule.toEntity()
        val eventEntities = schedule.toEventEntities()
        val restored = ScheduleWithEvents(
            schedule = scheduleEntity,
            events = eventEntities,
        ).toDomain()

        assertEquals(schedule.id, restored.id)
        assertEquals(schedule.protocolId, restored.protocolId)
        assertEquals(schedule.protocolName, restored.protocolName)
        assertEquals(schedule.protocolType, restored.protocolType)
        assertEquals(schedule.anchor, restored.anchor)
        assertEquals(schedule.anchorDate, restored.anchorDate)
        assertEquals(schedule.farmName, restored.farmName)
        assertEquals(schedule.responsibleName, restored.responsibleName)
        assertEquals(schedule.createdAt, restored.createdAt)
        assertEquals(schedule.updatedAt, restored.updatedAt)
        assertEquals(schedule.events.size, restored.events.size)
        assertEquals(schedule.orderedEvents.first().title, restored.orderedEvents.first().title)
        assertEquals(schedule.orderedEvents.last().date, restored.orderedEvents.last().date)
    }
}

private class IncrementalIdProvider : IdProvider {
    private var value = 0

    override fun nextId(): String {
        value += 1
        return "id-$value"
    }
}
