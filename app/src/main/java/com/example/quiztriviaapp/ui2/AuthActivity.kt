package com.example.quiztriviaapp.ui2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.setContent
import com.example.quiztriviaapp.util.Validation
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import com.example.quiztriviaapp.R
import com.example.quiztriviaapp.ui.theme.QuizTriviaAppTheme


class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizTriviaAppTheme {
                AuthScreen(
                    onLogin = { startActivity(
                        Intent(
                            this,
                            RulesActivity::class.java
                        )
                    )
                    },
                    onRegister = { startActivity(Intent(this, RegisterActivity::class.java)) })
            }
        }
    }
}

@Composable
fun AuthScreen(onLogin: ()->Unit, onRegister: ()->Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Welcome to QuizTrivia!", style = MaterialTheme.typography.headlineSmall)
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

        Text("Login or Register", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Login button
            Button(
                onClick = {
                    if (!Validation.isEmailValid(email)) {
                        error = "Enter a valid email"
                        return@Button
                    }
                    if (!Validation.isPasswordValid(password)) {
                        error = "Password must be >= 6 characters"
                        return@Button
                    }
                    error = null
                    onLogin()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Login")
            }

            // Register button
            Button(
                onClick = onRegister,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Register")
            }
        }
    }
}