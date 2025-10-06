package com.lee.crowdtracker.libray.navermap.di

import com.lee.crowdtracker.libray.navermap.NaverMapSdkController
import com.lee.crowdtracker.libray.navermap.NaverMapSdkControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NaverMapModule {
    @Binds
    @Singleton
    abstract fun bindNaverMapSdkController(naverMapSdkControllerImpl: NaverMapSdkControllerImpl): NaverMapSdkController
}