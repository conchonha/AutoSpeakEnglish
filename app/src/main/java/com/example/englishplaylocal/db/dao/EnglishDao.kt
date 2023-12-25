package com.example.englishplaylocal.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.englishplaylocal.model.EnglishTable
import kotlinx.coroutines.flow.Flow

@Dao
interface EnglishDao : BaseDao<EnglishTable> {
    @Query("SELECT * FROM EnglishTable WHERE type= :type")
    fun getListItem(type : Int) : Flow<List<EnglishTable>>
}