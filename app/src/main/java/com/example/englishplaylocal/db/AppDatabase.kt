package com.example.englishplaylocal.db

import androidx.room.*
import com.example.englishplaylocal.model.EnglishTable
import com.example.englishplaylocal.db.dao.EnglishDao

@Database(entities = [EnglishTable::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getEnglishDao(): EnglishDao
}