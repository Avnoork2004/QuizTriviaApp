package com.example.quiztriviaapp.ui2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.quiztriviaapp.R
import com.example.quiztriviaapp.data.PrefsDataStore
import com.example.quiztriviaapp.models.AnswerRecord
import com.example.quiztriviaapp.models.QuizResult
import com.example.quiztriviaapp.ui.theme.QuizTriviaAppTheme
import kotlinx.coroutines.launch

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)

        // Saves result to DataStore
        lifecycleScope.launch {
            val result = QuizResult(
                timestampMs = System.currentTimeMillis(),
                score = score,
                total = total,
                answers = emptyList()
            )
            PrefsDataStore.saveResult(applicationContext, result)
        }

        setContent {
            QuizTriviaAppTheme {
                ResultScreen(
                    score = score,
                    total = total,
                    onHistory = { startActivity(Intent(this, HistoryActivity::class.java)) },
                    onRetake = {
                        startActivity(Intent(this, RulesActivity::class.java))
                        finish()
                    }

                )
            }
        }
    }
}

@Composable
fun ResultScreen(score: Int, total: Int, onHistory: () -> Unit, onRetake: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {


        Text(
            text = "QuizTrivia!",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        // Splash Image
        Image(
            painter = painterResource(id = R.drawable.splash_image),
            contentDescription = "Splash Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(16.dp))

        Text("Quiz Finished", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        Text("Your score: $score / $total", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(20.dp))
        Row {
            //retake button
            Button(
                onClick = onRetake,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Retake")
            }

            Spacer(Modifier.width(12.dp))

            //History button
            OutlinedButton(
                onClick = onHistory,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            ) {
                Text("History")
            }
        }
    }
}
