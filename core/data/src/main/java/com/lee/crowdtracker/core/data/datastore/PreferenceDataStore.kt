package com.lee.crowdtracker.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Preference DataSource class
 */
class PreferenceDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore(name = SETTINGS)
    private val isDownloadAreaKey = booleanPreferencesKey(KEY_IS_DOWNLOAD_AREA)

    val isDownloadArea: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isDownloadAreaKey] ?: false
        }.catch {
            emptyPreferences()
        }

    suspend fun setIsDownloadArea(isDownload: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isDownloadAreaKey] = isDownload
        }
    }

    companion object {
        private const val KEY_IS_DOWNLOAD_AREA = "is_download_area"
        private const val SETTINGS = "settings"
    }
}
