package com.example.quiztriviaapp.util

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

object Validation {

    fun notEmpty(vararg fields: String): Boolean {
        return fields.all { it.trim().isNotEmpty() }
    }

    fun isNameValid(name: String): Boolean {
        val trimmed = name.trim()
        return trimmed.length in 3..30
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.trim().length >= 6
    }

    fun isDobValid(dob: String): Boolean {
        val trimmed = dob.trim()
        val regex = Regex("""^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\d{4})$""")
        if (!regex.matches(trimmed)) return false

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        format.isLenient = false
        return try {
            val parsedDate = format.parse(trimmed)
            val today = Calendar.getInstance().time
            parsedDate != null && !parsedDate.after(today)
        } catch (e: Exception) {
            false
        }
    }
}