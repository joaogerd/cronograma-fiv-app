package br.com.cronogramafiv.ui.screens.schedule

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import br.com.cronogramafiv.ui.theme.CronogramaFivTheme
import org.junit.Rule
import org.junit.Test

class ScheduleCreationScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun scheduleCreationScreenShowsMainFields() {
        composeTestRule.setContent {
            CronogramaFivTheme {
                ScheduleCreationScreen(
                    uiState = ScheduleCreationUiState(),
                    onProtocolSelected = {},
                    onAnchorSelected = {},
                    onDateTextChanged = {},
                    onFarmNameChanged = {},
                    onResponsibleNameChanged = {},
                    onGenerateSchedule = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Cronograma FIV").assertIsDisplayed()
        composeTestRule.onNodeWithText("Protocolo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Base do cálculo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Data de referência").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gerar cronograma").assertIsDisplayed()
    }
}
