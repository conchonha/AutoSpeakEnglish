package com.example.englishplaylocal.db.dao

import androidx.room.*

@Dao
interface BaseDao<T>{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg data : T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data : T) : Long

    @Update
    suspend fun update(data : T) : Int

    @Update
    suspend fun update(vararg data : T) : Int

    @Delete
    suspend fun delete(vararg data : T)

    @Delete
    suspend fun delete(data : T) : Int
}