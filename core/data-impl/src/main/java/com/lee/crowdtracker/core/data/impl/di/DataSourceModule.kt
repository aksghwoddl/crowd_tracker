package com.lee.crowdtracker.core.data.impl.di

import com.lee.crowdtracker.core.data.datasource.preference.PreferenceDataSource
import com.lee.crowdtracker.core.data.impl.datasource.PreferenceDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindPreferenceDataSource(preferenceDataSourceImpl: PreferenceDataSourceImpl): PreferenceDataSource
}