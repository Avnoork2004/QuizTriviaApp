package com.example.quiztriviaapp.models

import kotlinx.serialization.Serializable

/**
 * Record of user's answer to a question
 */
@Serializable
data class AnswerRecord(
    val questionId: Int,              // ID of question answered
    val selectedIndices: List<Int>,   // Indices of chosen answers
    val isCorrect: Boolean            // if answer was correct
)

/**
 * User's completed quiz result
 */
@Serializable
data class QuizResult(
    val timestampMs: Long,            // Time the quiz was completed (m.s)
    val score: Int,                   // score
    val total: Int,                   // # of questions
    val answers: List<AnswerRecord>   // Record of answers
)