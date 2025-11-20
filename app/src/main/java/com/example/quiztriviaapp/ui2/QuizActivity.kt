package com.example.quiztriviaapp.ui2

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.quiztriviaapp.models.AnswerRecord
import com.example.quiztriviaapp.models.QuestionType
import com.example.quiztriviaapp.models.QuizResult
import com.example.quiztriviaapp.data.PrefsDataStore
import com.example.quiztriviaapp.ui.theme.QuizTriviaAppTheme
import com.example.quiztriviaapp.util.QuizManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizActivity : ComponentActivity() {
    private val questions = QuizManager.sampleQuestions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizTriviaAppTheme {
                QuizScreen(questions = questions, activity = this)
            }
        }
    }
}

@Composable
fun QuizScreen(questions: List<com.example.quiztriviaapp.models.Question>, activity: ComponentActivity) {
    var idx by remember { mutableStateOf(0) }
    val current = questions[idx]
    var selectedSingle by remember { mutableStateOf(-1) }
    var selectedMulti by remember { mutableStateOf(mutableSetOf<Int>()) }
    var textAnswer by remember { mutableStateOf("") }
    var showConfirm by remember { mutableStateOf(false) }
    val recorded = remember { mutableStateListOf<AnswerRecord>() }

    var timeLeft by remember { mutableStateOf(30000L) }
    var timer by remember { mutableStateOf<CountDownTimer?>(null) }

    DisposableEffect(current.id) {
        timer?.cancel()
        timeLeft = 30000L
        timer = object : CountDownTimer(30000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) { timeLeft = millisUntilFinished }
            override fun onFinish() {
                recorded.add(AnswerRecord(current.id, emptyList(), false))
                if (idx + 1 >= questions.size) {
                    finishQuiz(recorded, activity)
                } else {
                    idx++
                    selectedSingle = -1
                    selectedMulti.clear()
                    textAnswer = ""
                }
            }
        }.also { it.start() }
        onDispose { timer?.cancel() }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Question ${idx+1}/${questions.size}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(progress = (timeLeft.toFloat()/30000f))
        Spacer(Modifier.height(8.dp))
        Text(current.text, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(12.dp))

        when (current.type) {
            QuestionType.SINGLE_CHOICE -> {
                current.options.forEachIndexed { i, option ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        RadioButton(selected = selectedSingle == i, onClick = { selectedSingle = i })
                        Spacer(Modifier.width(8.dp))
                        Text(option)
                    }
                }
            }
            QuestionType.MULTI_CHOICE -> {
                current.options.forEachIndexed { i, option ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Checkbox(
                            checked = selectedMulti.contains(i),
                            onCheckedChange = {
                                if (it) selectedMulti.add(i) else selectedMulti.remove(i)
                                selectedMulti = selectedMulti
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(option)
                    }
                }
            }
            QuestionType.TEXT -> {
                OutlinedTextField(
                    value = textAnswer,
                    onValueChange = { textAnswer = it },
                    label = { Text("Your answer") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Row {
            Button(
                onClick = {
                    val canConfirm = when (current.type) {
                        QuestionType.SINGLE_CHOICE -> selectedSingle >= 0
                        QuestionType.MULTI_CHOICE -> selectedMulti.isNotEmpty()
                        QuestionType.TEXT -> textAnswer.isNotBlank()
                    }
                    if (!canConfirm) return@Button
                    showConfirm = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Confirm Answer")
            }

            Spacer(Modifier.width(12.dp))
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Confirm answer") },
            text = { Text("Are you sure you want to submit this answer?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false

                    val selectedIndices = when (current.type) {
                        QuestionType.SINGLE_CHOICE -> if (selectedSingle >= 0) listOf(selectedSingle) else emptyList()
                        QuestionType.MULTI_CHOICE -> selectedMulti.sorted()
                        QuestionType.TEXT -> emptyList()
                    }
                    val correct = when (current.type) {
                        QuestionType.TEXT -> QuizManager.isTextAnswerCorrect(textAnswer)
                        else -> selectedIndices.sorted() == current.correctAnswers.sorted()
                    }
                    recorded.add(AnswerRecord(current.id, selectedIndices, correct))
                    timer?.cancel()

                    if (idx + 1 >= questions.size) {
                        finishQuiz(recorded, activity)
                    } else {
                        idx++
                        selectedSingle = -1
                        selectedMulti.clear()
                        textAnswer = ""
                    }
                }) { Text("Yes") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("No") }
            }
        )
    }
}

private fun finishQuiz(recorded: List<AnswerRecord>, activity: ComponentActivity) {
    val score = recorded.count { it.isCorrect }
    val total = recorded.size
    val result = QuizResult(System.currentTimeMillis(), score, total, recorded)

    // Saves result in PrefsDataStore
    CoroutineScope(Dispatchers.IO).launch {
        PrefsDataStore.saveResult(activity.applicationContext, result)
    }

    // Goes to ResultActivity and removes QuizActivity from the back stack
    val intent = Intent(activity, ResultActivity::class.java).apply {
        putExtra("score", result.score)
        putExtra("total", result.total)
        putExtra("timestamp", result.timestampMs)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    activity.startActivity(intent)
    activity.finish()
}