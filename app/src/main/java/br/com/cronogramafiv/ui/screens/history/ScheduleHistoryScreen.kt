package br.com.cronogramafiv.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.cronogramafiv.domain.model.BuiltInProtocols
import br.com.cronogramafiv.domain.model.Schedule
import br.com.cronogramafiv.domain.model.ScheduleAnchor
import br.com.cronogramafiv.domain.repository.ScheduleRepository
import br.com.cronogramafiv.domain.service.CurrentDateProvider
import br.com.cronogramafiv.domain.service.IdProvider
import br.com.cronogramafiv.domain.service.ScheduleGenerator
import br.com.cronogramafiv.ui.theme.CronogramaFivTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun ScheduleHistoryRoute(
    scheduleRepository: ScheduleRepository,
    modifier: Modifier = Modifier,
) {
    val viewModel: ScheduleHistoryViewModel = viewModel(
        factory = ScheduleHistoryViewModelFactory(
            scheduleRepository = scheduleRepository,
        ),
    )
    val uiState by viewModel.uiState.collectAsState()

    ScheduleHistoryScreen(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        modifier = modifier,
    )
}

@Composable
fun ScheduleHistoryScreen(
    uiState: ScheduleHistoryUiState,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HeaderSection()

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChanged,
            label = { Text("Buscar fazenda/protocolo") },
            singleLine = true,
        )

        uiState.errorMessage?.let { message ->
            MessageCard(message = message)
        }

        when {
            uiState.isEmpty -> EmptyHistoryCard()
            uiState.hasNoSearchResults -> MessageCard(
                message = "Nenhum cronograma encontrado para a busca.",
            )
            else -> uiState.filteredSchedules.forEach { schedule ->
                ScheduleHistoryCard(schedule = schedule)
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Histórico",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Consulte os cronogramas salvos no dispositivo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun EmptyHistoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Nenhum cronograma salvo ainda",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Crie um cronograma na aba Criar e toque em Salvar cronograma para vê-lo aqui.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun MessageCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = message,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ScheduleHistoryCard(schedule: Schedule) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = schedule.farmName ?: "Sem fazenda informada",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = schedule.protocolName,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Período: ${schedule.startDate.formatPtBr()} a ${schedule.endDate.formatPtBr()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${schedule.events.size} etapas",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

private fun LocalDate.formatPtBr(): String = format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

@Preview(showBackground = true)
@Composable
private fun ScheduleHistoryScreenPreview() {
    CronogramaFivTheme {
        ScheduleHistoryScreen(
            uiState = ScheduleHistoryUiState(
                schedules = listOf(sampleSchedule()),
            ),
            onSearchQueryChanged = {},
        )
    }
}

private fun sampleSchedule(): Schedule {
    val generator = ScheduleGenerator(
        idProvider = IncrementalIdProvider(),
        currentDateProvider = CurrentDateProvider { LocalDate.of(2026, 5, 4) },
    )

    return generator.generate(
        protocol = BuiltInProtocols.recipient,
        anchor = ScheduleAnchor.PROTOCOL_START,
        anchorDate = LocalDate.of(2026, 1, 10),
        farmName = "Fazenda Moquem",
        responsibleName = "João",
    )
}

private class IncrementalIdProvider : IdProvider {
    private var value = 0

    override fun nextId(): String {
        value += 1
        return "id-$value"
    }
}

private class PreviewScheduleRepository : ScheduleRepository {
    override fun observeSchedules(): Flow<List<Schedule>> = flowOf(emptyList())
    override suspend fun getScheduleById(id: String): Schedule? = null
    override suspend fun saveSchedule(schedule: Schedule) = Unit
    override suspend fun deleteScheduleById(id: String) = Unit
}
