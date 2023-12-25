package com.example.englishplaylocal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EnglishTable(
    @PrimaryKey(autoGenerate = true)
    var stt: Int = 0,
    val name: String = "",
    val subName: String = "",
    val content: String = "",
    val type: Int = 5,
    val time: Long = System.currentTimeMillis()
)