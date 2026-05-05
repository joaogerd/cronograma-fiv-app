package br.com.cronogramafiv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import br.com.cronogramafiv.ui.navigation.CronogramaFivApp
import br.com.cronogramafiv.ui.theme.CronogramaFivTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as CronogramaFivApplication

        setContent {
            CronogramaFivTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    CronogramaFivApp(
                        scheduleRepository = app.scheduleRepository,
                    )
                }
            }
        }
    }
}
