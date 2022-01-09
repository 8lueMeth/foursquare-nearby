package com.example.nearbyfoursquare.di

import android.content.Context
import androidx.room.Room
import com.example.nearbyfoursquare.data.local.PlacesDatabase
import com.example.nearbyfoursquare.data.local.dao.PlacesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlacesDatabase {
    @Singleton
    @Provides
    fun providePlacesDatabase(@ApplicationContext context: Context): PlacesDatabase {
        return Room.databaseBuilder(
            context,
            PlacesDatabase::class.java,
            "database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePlacesDao(database: PlacesDatabase): PlacesDao = database.placesDao()
}