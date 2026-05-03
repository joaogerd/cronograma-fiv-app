package br.com.cronogramafiv.domain.service

import br.com.cronogramafiv.domain.model.BuiltInProtocols
import br.com.cronogramafiv.domain.model.ScheduleAnchor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class ScheduleGeneratorTest {
    private val fixedDate = LocalDate.of(2026, 5, 2)
    private val generator = ScheduleGenerator(
        idProvider = IncrementalIdProvider(),
        currentDateProvider = CurrentDateProvider { fixedDate },
    )

    @Test
    fun generateRecipientScheduleFromProtocolStartDate() {
        val schedule = generator.generate(
            protocol = BuiltInProtocols.recipient,
            anchor = ScheduleAnchor.PROTOCOL_START,
            anchorDate = LocalDate.of(2026, 1, 10),
            farmName = " Fazenda Moquem ",
            responsibleName = " João ",
        )

        assertEquals(LocalDate.of(2026, 1, 10), schedule.events[0].date)
        assertEquals(LocalDate.of(2026, 1, 18), schedule.events[1].date)
        assertEquals(LocalDate.of(2026, 1, 27), schedule.events[2].date)
        assertEquals(LocalDate.of(2026, 1, 10), schedule.startDate)
        assertEquals(LocalDate.of(2026, 1, 27), schedule.endDate)
        assertEquals("Fazenda Moquem", schedule.farmName)
        assertEquals("João", schedule.responsibleName)
        assertEquals(fixedDate, schedule.createdAt)
        assertEquals(fixedDate, schedule.updatedAt)
    }

    @Test
    fun generateRecipientScheduleFromEmbryoTransferDate() {
        val schedule = generator.generate(
            protocol = BuiltInProtocols.recipient,
            anchor = ScheduleAnchor.EMBRYO_TRANSFER,
            anchorDate = LocalDate.of(2026, 1, 27),
        )

        assertEquals(LocalDate.of(2026, 1, 10), schedule.startDate)
        assertEquals(LocalDate.of(2026, 1, 27), schedule.endDate)
        assertEquals(LocalDate.of(2026, 1, 27), schedule.anchorDate)
    }

    @Test
    fun generateDonorPiveScheduleFromProtocolStartDate() {
        val schedule = generator.generate(
            protocol = BuiltInProtocols.donorPive,
            anchor = ScheduleAnchor.PROTOCOL_START,
            anchorDate = LocalDate.of(2026, 2, 1),
        )

        assertEquals(LocalDate.of(2026, 2, 10), schedule.events[0].date)
        assertEquals(LocalDate.of(2026, 2, 11), schedule.events[1].date)
        assertEquals(LocalDate.of(2026, 2, 12), schedule.events[2].date)
        assertEquals(LocalDate.of(2026, 2, 14), schedule.events[3].date)
        assertEquals(LocalDate.of(2026, 2, 16), schedule.events[4].date)
        assertEquals(LocalDate.of(2026, 2, 18), schedule.events[5].date)
    }

    @Test
    fun generatePregnancyFollowUpFromExpectedBirthDate() {
        val schedule = generator.generate(
            protocol = BuiltInProtocols.pregnancyFollowUp,
            anchor = ScheduleAnchor.EXPECTED_BIRTH,
            anchorDate = LocalDate.of(2026, 12, 31),
        )

        assertEquals(LocalDate.of(2026, 3, 26), schedule.startDate)
        assertEquals(LocalDate.of(2026, 12, 31), schedule.endDate)
        assertEquals(LocalDate.of(2026, 12, 31), schedule.events.last().date)
    }

    @Test
    fun blankOptionalNamesBecomeNull() {
        val schedule = generator.generate(
            protocol = BuiltInProtocols.recipient,
            anchor = ScheduleAnchor.PROTOCOL_START,
            anchorDate = LocalDate.of(2026, 1, 10),
            farmName = "   ",
            responsibleName = "   ",
        )

        assertNull(schedule.farmName)
        assertNull(schedule.responsibleName)
    }
}

private class IncrementalIdProvider : IdProvider {
    private var value = 0

    override fun nextId(): String {
        value += 1
        return "id-$value"
    }
}
