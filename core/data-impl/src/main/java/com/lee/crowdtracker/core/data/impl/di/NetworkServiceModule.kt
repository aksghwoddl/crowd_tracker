package com.lee.crowdtracker.core.data.impl.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lee.crowdtracker.core.data.impl.service.SeoulOpenApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkServiceModule {
    @Provides
    @Singleton
    fun provideSeoulOpenApiService(
        okHttpClient: OkHttpClient,
        json: Json,
    ) = Retrofit.Builder()
        .baseUrl("http://openapi.seoul.go.kr:8088/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(SeoulOpenApiService::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .build()
    }
}