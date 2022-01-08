package com.example.nearbyfoursquare.data.remote.repository

import androidx.paging.ExperimentalPagingApi
import androidx.room.withTransaction
import com.example.nearbyfoursquare.data.local.PlacesDatabase
import com.example.nearbyfoursquare.data.local.entity.PlacesEntity
import com.example.nearbyfoursquare.data.remote.service.PlacesApiService
import com.example.nearbyfoursquare.util.Resource
import com.example.nearbyfoursquare.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class PlacesRepository @Inject constructor(
    private val placesApiService: PlacesApiService,
    private val placesDatabase: PlacesDatabase,
) {

    private val placesDao = placesDatabase.placesDao()

    fun getPlaces(ll: String): Flow<Resource<List<PlacesEntity>>> = networkBoundResource(
        query = { placesDao.getAllPlaces() },
        fetch = {
            val response = placesApiService.getNearbyPlaces(ll = ll)

            val places = response.body()?.results
            places?.map {
                PlacesEntity(
                    id = it.fsq_id,
                    name = it.name,
                    distance = it.distance,
                    address = it.location.address,
                    country = it.location.country,
                    dma = it.location.dma,
                    region = it.location.region,
                    categoryName = it.categories?.getOrNull(0)?.name,
                    categoryIcon = it.categories?.getOrNull(0)?.getIcon(),
                )
            } ?: emptyList()
        },
        saveFetchResult = { entities ->
            placesDatabase.withTransaction {
                placesDao.clearAll()
                placesDao.insertAll(entities)
            }
        },
        shouldFetch = { true },
    )
}