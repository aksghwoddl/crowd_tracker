package com.lee.crowdtracker.core.data.db.csv

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


private const val VERSION = 1

@Database(entities = [CsvDownloadEntity::class], version = VERSION)
abstract class CsvDownloadDatabase : RoomDatabase() {
    abstract fun csvDownloadDao(): CsvDownloadDao
}