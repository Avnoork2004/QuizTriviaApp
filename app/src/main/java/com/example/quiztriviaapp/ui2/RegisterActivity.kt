package com.example.quiztriviaapp.ui2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import com.example.quiztriviaapp.ui.theme.QuizTriviaAppTheme
import com.example.quiztriviaapp.util.Validation

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizTriviaAppTheme {
                RegisterScreen(onSuccess = { finish() })
            }
        }
    }
}

@Composable
fun RegisterScreen(onSuccess: () -> Unit) {
    var first by remember { mutableStateOf("") }
    var family by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var err by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = first,
            onValueChange = { first = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = err?.contains("First") == true
        )

        OutlinedTextField(
            value = family,
            onValueChange = { family = it },
            label = { Text("Family Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = err?.contains("Family") == true
        )

        // DOB Field
        OutlinedTextField(
            value = dob,
            onValueChange = { newValue ->
                val digits = newValue.text.filter { it.isDigit() }.take(8)

                val formatted = when {
                    digits.length <= 2 -> digits
                    digits.length <= 4 -> digits.substring(0, 2) + "/" + digits.substring(2)
                    else -> digits.substring(0, 2) + "/" + digits.substring(2, 4) + "/" + digits.substring(4)
                }

                dob = TextFieldValue(formatted, selection = TextRange(formatted.length))
            },
            label = { Text("Date of Birth (MM/DD/YYYY)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = err?.contains("birth", ignoreCase = true) == true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = err?.contains("email", ignoreCase = true) == true
        )

        OutlinedTextField(
            value = pw,
            onValueChange = { pw = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = err?.contains("Password") == true
        )

        Spacer(Modifier.height(12.dp))
        err?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        // Register button
        Button(
            onClick = {
                val dobText = dob.text

                when {
                    !Validation.notEmpty(first, family, dobText, email, pw) ->
                        err = "All fields are required"

                    !Validation.isNameValid(first) ->
                        err = "First name must be between 3 and 30 characters"

                    !Validation.isNameValid(family) ->
                        err = "Family name must be between 3 and 30 characters"

                    dobText.length != 10 || !Validation.isDobValid(dobText) ->
                        err = "Invalid date of birth. Use format MM/DD/YYYY"

                    !Validation.isEmailValid(email) ->
                        err = "Invalid email format"

                    !Validation.isPasswordValid(pw) ->
                        err = "Password must be at least 6 characters long"

                    else -> {
                        err = null
                        onSuccess()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Register")
        }
    }
}