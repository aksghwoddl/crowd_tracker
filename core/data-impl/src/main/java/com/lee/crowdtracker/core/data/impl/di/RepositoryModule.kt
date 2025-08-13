package com.lee.crowdtracker.core.data.impl.di

import com.lee.crowdtracker.core.data.impl.repository.AreaRepositoryImpl
import com.lee.crowdtracker.core.data.impl.repository.SeoulCityDataRepositoryImpl
import com.lee.crowdtracker.core.data.repository.AreaRepository
import com.lee.crowdtracker.core.data.repository.SeoulCityDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAreaRepository(areaRepositoryImpl: AreaRepositoryImpl): AreaRepository

    @Binds
    @Singleton
    abstract fun bindSeoulCityDataRepository(seoulCityDataRepositoryImpl: SeoulCityDataRepositoryImpl): SeoulCityDataRepository
}