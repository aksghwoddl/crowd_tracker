package com.lee.crowdtracker.core.data.impl.datasource

import com.lee.crowdtracker.core.data.datasource.preference.PreferenceDataSource
import com.lee.crowdtracker.core.data.datastore.PreferenceDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferenceDataSourceImpl @Inject constructor(
    private val dataStorePreference: PreferenceDataStore
) : PreferenceDataSource {
    override fun getIsDownloadArea(): Flow<Boolean> {
        return dataStorePreference.isDownloadArea
    }

    override suspend fun setIsDownloadArea(download: Boolean) {
        dataStorePreference.setIsDownloadArea(isDownload = download)
    }
}