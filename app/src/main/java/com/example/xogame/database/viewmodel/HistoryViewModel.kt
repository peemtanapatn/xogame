package com.example.xogame.database.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.xogame.database.AppDatabase
import com.example.xogame.database.entities.History
import com.example.xogame.database.repository.HistoryRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HistoryViewModel(application: Application): AndroidViewModel(application) {

    private val historyRepository: HistoryRepository

    init {
        val historyDao = AppDatabase.getDatabase(application).historyDao()
        historyRepository = HistoryRepository(historyDao)
    }

    val allEvents: LiveData<List<History>> = historyRepository.allHistory

    fun insertEvent(history: History) = viewModelScope.launch {
        historyRepository.insertHistory(history)
    }
}