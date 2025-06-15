package com.lee.crowdtracker.core.data.impl.di

import com.lee.bb.core.data.impl.repository.BeachRepositoryImpl
import com.lee.crowdtracker.core.data.impl.repository.AreaRepositoryImpl
import com.lee.crowdtracker.core.data.repository.AreaRepository
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
    abstract fun bindBeachRepository(beachRepositoryImpl: BeachRepositoryImpl): BeachRepositoryImpl

    @Binds
    @Singleton
    abstract fun bindAreaRepository(areaRepositoryImpl: AreaRepositoryImpl): AreaRepository
}