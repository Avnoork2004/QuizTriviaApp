package com.example.quiztriviaapp.models

import kotlinx.serialization.Serializable

enum class QuestionType { SINGLE_CHOICE, MULTI_CHOICE, TEXT }

@Serializable
data class Question(
    val id: Int,
    val text: String,
    val type: QuestionType,
    val options: List<String> = emptyList(),
    val correctAnswers: List<Int> = emptyList()
)