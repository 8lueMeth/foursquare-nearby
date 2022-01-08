package com.example.nearbyfoursquare.di

import com.example.nearbyfoursquare.data.remote.service.PlacesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiServiceModule {

    @Singleton
    @Provides
    fun providePlacesApiService(retrofit: Retrofit): PlacesApiService =
        retrofit.create(PlacesApiService::class.java)
}