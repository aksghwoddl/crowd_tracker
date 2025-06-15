package com.lee.crowdtracker.core.data.db

import android.content.Context
import androidx.room.Room
import com.lee.crowdtracker.core.data.db.csv.CsvDownloadDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCsvDownloadDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context = context,
        klass = CsvDownloadDatabase::class.java,
        name = "csv_Download_Database"
    ).build()

    @Provides
    @Singleton
    fun provideCsvDownloadDao(database: CsvDownloadDatabase) = database.csvDownloadDao()
}