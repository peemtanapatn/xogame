package com.example.xogame.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

@Entity(tableName = "history_table")
data class History (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val detailGame: String,
    val date: LocalDateTime

)