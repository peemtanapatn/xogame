package com.example.xogame.database.repository

import androidx.lifecycle.LiveData
import com.example.xogame.database.dao.HistoryDao
import com.example.xogame.database.entities.History
import java.time.LocalDateTime

class HistoryRepository(private val historyDao: HistoryDao) {

    val allHistory: LiveData<List<History>> = historyDao.getHistoryAll()

    suspend fun insertHistory(history: History) {
        historyDao.insertHistory(history)
    }
}