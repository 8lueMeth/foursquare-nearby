package com.example.nearbyfoursquare.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MoshiModule {

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

}
