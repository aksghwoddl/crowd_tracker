package com.lee.crowdtracker.core.data.db.csv

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "csv_download_area")
data class CsvDownloadEntity(
    @PrimaryKey
    val areaId: String,
    val category: String,
    val no: Int,
    val name: String,
    val englishName: String
)