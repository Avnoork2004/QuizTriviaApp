package com.example.quiztriviaapp.util

import com.example.quiztriviaapp.models.Question
import com.example.quiztriviaapp.models.QuestionType

object QuizManager {
    fun sampleQuestions(): List<Question> {
        return listOf(
            Question(
                id = 1,
                text = "Which data structure uses LIFO (Last In, First Out)?",
                type = QuestionType.SINGLE_CHOICE,
                options = listOf("Queue", "Stack", "Array", "Linked List"),
                correctAnswers = listOf(1)
            ),
            Question(
                id = 2,
                text = "Which of the following are valid Android layout types?",
                type = QuestionType.MULTI_CHOICE,
                options = listOf("LinearLayout", "GridLayout", "TableLayout", "FlexBoxLayout"),
                correctAnswers = listOf(0,1,3)
            ),
            Question(
                id = 3,
                text = "Which language is used for Android app development (official)?",
                type = QuestionType.SINGLE_CHOICE,
                options = listOf("Kotlin","Swift","Ruby","Rust"),
                correctAnswers = listOf(0)
            ),
            Question(
                id = 4,
                text = "Which of these are JVM languages?",
                type = QuestionType.MULTI_CHOICE,
                options = listOf("Kotlin","Ruby","Scala","C"),
                correctAnswers = listOf(0,2)
            ),
            Question(
                id = 5,
                text = "What does 'HTTP' stand for?",
                type = QuestionType.TEXT,
                options = emptyList(),
                correctAnswers = emptyList()
            )
        )
    }

    fun isTextAnswerCorrect(userText: String): Boolean {

        val normalized = userText.trim().lowercase()
        return normalized in setOf( "hypertext transfer protocol"
            , "Hypertext Transfer Protocol"
            , "Hypertext transfer protocol"
        , "hypertext Transfer Protocol"
        , "hypertext transfer Protocol"
        , "Hypertext Transfer protocol"
        , "Hypertext transfer Protocol")
    }
}