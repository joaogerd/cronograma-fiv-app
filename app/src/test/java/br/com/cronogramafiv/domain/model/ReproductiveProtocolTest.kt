package br.com.cronogramafiv.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ReproductiveProtocolTest {
    @Test
    fun orderedStepsSortsByDayOffsetAndOrder() {
        val protocol = ReproductiveProtocol(
            id = "test-protocol",
            name = "Test Protocol",
            type = ProtocolType.CUSTOM,
            steps = listOf(
                ProtocolStep(id = "third", title = "Third", dayOffset = 2, order = 0),
                ProtocolStep(id = "second", title = "Second", dayOffset = 1, order = 1),
                ProtocolStep(id = "first", title = "First", dayOffset = 1, order = 0),
            ),
        )

        assertEquals(
            listOf("first", "second", "third"),
            protocol.orderedSteps.map { it.id },
        )
    }

    @Test
    fun protocolRequiresAtLeastOneStep() {
        assertThrows(IllegalArgumentException::class.java) {
            ReproductiveProtocol(
                id = "empty-protocol",
                name = "Empty Protocol",
                type = ProtocolType.CUSTOM,
                steps = emptyList(),
            )
        }
    }

    @Test
    fun builtInProtocolsContainExpectedDefaults() {
        assertEquals(3, BuiltInProtocols.recipient.steps.size)
        assertEquals(6, BuiltInProtocols.donorPive.steps.size)
        assertEquals(5, BuiltInProtocols.pregnancyFollowUp.steps.size)
    }
}
