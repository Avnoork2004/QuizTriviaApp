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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiztriviaapp.R
import com.example.quiztriviaapp.ui.theme.QuizTriviaAppTheme

class RulesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizTriviaAppTheme {

                RulesScreen(activity = this)
            }
        }
    }
}




@Composable
fun RulesScreen(activity: ComponentActivity) {

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // title
        Text(
            text = "Welcome to QuizTrivia!",
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

        //rules
        Text("Rules", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            "• There are at least 5 questions.\n \n" +
                    "• Hint: These questions are C.S related! \n \n" +
                    "• Confirm your answer before it is submitted.\n \n" +
                    "• There is a TIMER per question. \n \n" +
                    "• Take too long, and the question will skip! \n \n" +
                    "• Play at your own risk... \n \n",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 18.sp // Increased size
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            // Start QuizActivity
            context.startActivity(Intent(context, QuizActivity::class.java))
            // Closes RulesActivity
            activity.finish()
        }) {
            Text("Start Quiz")
        }
    }
}