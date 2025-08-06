package com.lee.crowdtracker.core.data.db.csv

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CsvDownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArea(area: CsvDownloadEntity)

    @Query("SELECT * FROM csv_download_area")
    suspend fun getAreaList(): List<CsvDownloadEntity>

    @Query("SELECT * FROM csv_download_area WHERE name LIKE '%' || :name || '%' OR englishName LIKE '%' || :name || '%'")
    fun getAreaByName(name: String): Flow<List<CsvDownloadEntity>>
}