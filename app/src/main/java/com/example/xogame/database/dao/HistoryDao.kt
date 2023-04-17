package com.example.xogame.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.xogame.database.entities.History

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun getHistoryAll(): LiveData<List<History>>
}