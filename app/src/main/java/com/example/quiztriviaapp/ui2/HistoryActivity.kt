package com.example.quiztriviaapp.ui2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.quiztriviaapp.data.PrefsDataStore
import com.example.quiztriviaapp.models.QuizResult
import com.example.quiztriviaapp.ui.theme.QuizTriviaAppTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizTriviaAppTheme {
                HistoryScreen(activity = this)
            }
        }
    }
}

@Composable
fun HistoryScreen(activity: ComponentActivity) {
    var history by remember { mutableStateOf<List<QuizResult>>(emptyList()) }

    // Loads from DataStore
    LaunchedEffect(Unit) {
        activity.lifecycleScope.launch {
            history = PrefsDataStore.getHistory(activity.applicationContext)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("History", style = MaterialTheme.typography.headlineSmall)
            if (history.isNotEmpty()) {

                Button(
                    onClick = {
                        val latest = history.last() // get the latest quiz
                        val intent = Intent(activity, ResultActivity::class.java).apply {
                            putExtra("score", latest.score)
                            putExtra("total", latest.total)
                        }
                        activity.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Latest Result")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (history.isEmpty()) {
            Text("No history yet. Play a quiz to create history.")
        } else {
            LazyColumn {
                items(history.reversed()) { item ->
                    HistoryRow(item)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun HistoryRow(result: QuizResult) {
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Date: ${sdf.format(Date(result.timestampMs))}", style = MaterialTheme.typography.bodySmall)
        Text("Score: ${result.score} / ${result.total}", style = MaterialTheme.typography.bodyMedium)
    }
}