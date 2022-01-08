package com.example.nearbyfoursquare.data.remote.service

import com.example.nearbyfoursquare.data.remote.model.ResponseWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("nearby")
    suspend fun getNearbyPlaces(
        @Query("ll") ll: String?,
        @Query("limit") limit: Int? = 10,
    ): Response<ResponseWrapper>

}