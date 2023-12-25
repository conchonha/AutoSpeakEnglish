package com.example.englishplaylocal.db

import android.app.Application
import androidx.room.Room
import com.example.englishplaylocal.db.dao.EnglishDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        application: Application,
    ): AppDatabase =
        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "SangTB"
        ).build()


    @Provides
    @Singleton
    fun providerEnglishDao(database: Provider<AppDatabase>): EnglishDao =
        database.get().getEnglishDao()
}