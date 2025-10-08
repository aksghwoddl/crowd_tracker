package com.lee.crowdtracker.core.data.datasource.preference

import kotlinx.coroutines.flow.Flow

interface PreferenceDataSource {
    fun getIsDownloadArea(): Flow<Boolean>
    suspend fun setIsDownloadArea(download: Boolean)
}