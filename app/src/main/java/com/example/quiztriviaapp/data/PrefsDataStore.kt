package com.example.quiztriviaapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.quiztriviaapp.models.QuizResult

// Creates DataStore instance
private val Context.dataStore by preferencesDataStore(name = "quiz_prefs")
private val KEY_HISTORY = stringPreferencesKey("quiz_history")

object PrefsDataStore {
    private val json = Json { prettyPrint = false; ignoreUnknownKeys = true }

    // Saves new quiz result
    suspend fun saveResult(context: Context, result: QuizResult) {
        val store = context.dataStore
        val currentJson = store.data.first()[KEY_HISTORY] ?: "[]"
        val list = try { json.decodeFromString<List<QuizResult>>(currentJson) } catch (e: Exception) { emptyList() }
        val newList = list + result
        store.edit { prefs ->
            prefs[KEY_HISTORY] = json.encodeToString(newList)
        }
    }

    // Gets all quiz results
    suspend fun getHistory(context: Context): List<QuizResult> {
        val store = context.dataStore
        val currentJson = store.data.first()[KEY_HISTORY] ?: "[]"
        return try { json.decodeFromString(currentJson) } catch (e: Exception) { emptyList() }
    }

    // Clears history
    suspend fun clearHistory(context: Context) {
        context.dataStore.edit { it.remove(KEY_HISTORY) }
    }
}