package br.com.cronogramafiv.ui.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.cronogramafiv.domain.model.BuiltInProtocols
import br.com.cronogramafiv.domain.model.ReproductiveProtocol
import br.com.cronogramafiv.domain.model.Schedule
import br.com.cronogramafiv.domain.model.ScheduleAnchor
import br.com.cronogramafiv.domain.model.ScheduleEvent
import br.com.cronogramafiv.domain.service.ScheduleGenerator
import br.com.cronogramafiv.ui.theme.CronogramaFivTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun ScheduleCreationScreen(
    generator: ScheduleGenerator = remember { ScheduleGenerator() },
) {
    val protocols = remember { BuiltInProtocols.all }
    val anchors = remember { ScheduleAnchor.entries }

    var selectedProtocol by remember { mutableStateOf(protocols.first()) }
    var selectedAnchor by remember { mutableStateOf(ScheduleAnchor.PROTOCOL_START) }
    var dateText by remember { mutableStateOf(LocalDate.now().toString()) }
    var farmName by remember { mutableStateOf("") }
    var responsibleName by remember { mutableStateOf("") }
    var generatedSchedule by remember { mutableStateOf<Schedule?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HeaderSection()

        ProtocolSelector(
            selectedProtocol = selectedProtocol,
            protocols = protocols,
            onProtocolSelected = {
                selectedProtocol = it
                generatedSchedule = null
                errorMessage = null
            },
        )

        AnchorSelector(
            selectedAnchor = selectedAnchor,
            anchors = anchors,
            onAnchorSelected = {
                selectedAnchor = it
                generatedSchedule = null
                errorMessage = null
            },
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dateText,
            onValueChange = {
                dateText = it
                errorMessage = null
            },
            label = { Text("Data de referência") },
            supportingText = { Text("Use o formato AAAA-MM-DD. Exemplo: 2026-01-10") },
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = farmName,
            onValueChange = { farmName = it },
            label = { Text("Fazenda / propriedade") },
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = responsibleName,
            onValueChange = { responsibleName = it },
            label = { Text("Responsável") },
            singleLine = true,
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val parsedDate = parseDate(dateText)
                if (parsedDate == null) {
                    errorMessage = "Data inválida. Use o formato AAAA-MM-DD."
                    generatedSchedule = null
                } else {
                    runCatching {
                        generator.generate(
                            protocol = selectedProtocol,
                            anchor = selectedAnchor,
                            anchorDate = parsedDate,
                            farmName = farmName,
                            responsibleName = responsibleName,
                        )
                    }.onSuccess {
                        generatedSchedule = it
                        errorMessage = null
                    }.onFailure {
                        generatedSchedule = null
                        errorMessage = it.message ?: "Não foi possível gerar o cronograma."
                    }
                }
            },
        ) {
            Text("Gerar cronograma")
        }

        errorMessage?.let { message ->
            ErrorCard(message = message)
        }

        generatedSchedule?.let { schedule ->
            ScheduleResult(schedule = schedule)
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Cronograma FIV",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Gere rapidamente cronogramas de receptora, doadora/PIVE e acompanhamento de prenhez.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ProtocolSelector(
    selectedProtocol: ReproductiveProtocol,
    protocols: List<ReproductiveProtocol>,
    onProtocolSelected: (ReproductiveProtocol) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Protocolo",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = true },
        ) {
            Text(selectedProtocol.name)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            protocols.forEach { protocol ->
                DropdownMenuItem(
                    text = { Text(protocol.name) },
                    onClick = {
                        onProtocolSelected(protocol)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun AnchorSelector(
    selectedAnchor: ScheduleAnchor,
    anchors: List<ScheduleAnchor>,
    onAnchorSelected: (ScheduleAnchor) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Base do cálculo",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = true },
        ) {
            Text(selectedAnchor.label)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            anchors.forEach { anchor ->
                DropdownMenuItem(
                    text = { Text(anchor.label) },
                    onClick = {
                        onAnchorSelected(anchor)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ScheduleResult(schedule: Schedule) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Cronograma gerado",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(text = schedule.protocolName, fontWeight = FontWeight.SemiBold)
                Text(text = "Período: ${schedule.startDate.formatPtBr()} a ${schedule.endDate.formatPtBr()}")
                schedule.farmName?.let { Text(text = "Fazenda: $it") }
                schedule.responsibleName?.let { Text(text = "Responsável: $it") }
            }
        }

        schedule.orderedEvents.forEach { event ->
            ScheduleEventCard(event = event)
        }
    }
}

@Composable
private fun ScheduleEventCard(event: ScheduleEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                event.description?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(
                text = event.date.formatPtBr(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private val ScheduleAnchor.label: String
    get() = when (this) {
        ScheduleAnchor.PROTOCOL_START -> "Início do protocolo"
        ScheduleAnchor.EMBRYO_TRANSFER -> "Transferência embrionária"
        ScheduleAnchor.EXPECTED_BIRTH -> "Nascimento previsto"
    }

private fun parseDate(value: String): LocalDate? {
    return try {
        LocalDate.parse(value.trim())
    } catch (_: DateTimeParseException) {
        null
    }
}

private fun LocalDate.formatPtBr(): String = format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

@Preview(showBackground = true)
@Composable
private fun ScheduleCreationScreenPreview() {
    CronogramaFivTheme {
        ScheduleCreationScreen()
    }
}
