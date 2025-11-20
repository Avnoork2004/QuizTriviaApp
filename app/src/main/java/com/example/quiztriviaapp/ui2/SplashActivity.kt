package com.example.quiztriviaapp.ui2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.activity.compose.setContent
import com.example.quiztriviaapp.R

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SplashScreen() }

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }, 3000) // 3.0s splash scrreen
    }
}

@Composable
fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(painter = painterResource(id = R.drawable.splash_image), contentDescription = "Splash image", modifier = Modifier.size(140.dp))
            Spacer(modifier = Modifier.height(12.dp))
            androidx.compose.material3.Text(text = "QuizTrivia", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        }
    }
}