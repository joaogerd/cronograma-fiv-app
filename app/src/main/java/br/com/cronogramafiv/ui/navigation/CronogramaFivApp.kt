package br.com.cronogramafiv.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.cronogramafiv.domain.repository.ScheduleRepository
import br.com.cronogramafiv.ui.screens.history.ScheduleHistoryRoute
import br.com.cronogramafiv.ui.screens.schedule.ScheduleCreationRoute

@Composable
fun CronogramaFivApp(
    scheduleRepository: ScheduleRepository,
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.CREATE) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { Text(tab.label) },
                        icon = {},
                    )
                }
            }
        },
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        when (selectedTab) {
            MainTab.CREATE -> ScheduleCreationRoute(
                scheduleRepository = scheduleRepository,
                modifier = modifier,
            )
            MainTab.HISTORY -> ScheduleHistoryRoute(
                scheduleRepository = scheduleRepository,
                modifier = modifier,
            )
        }
    }
}
