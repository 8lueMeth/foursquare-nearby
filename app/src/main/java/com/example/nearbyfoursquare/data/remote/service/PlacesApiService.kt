package com.example.nearbyfoursquare.data.remote.service

import com.example.nearbyfoursquare.data.remote.model.Details
import com.example.nearbyfoursquare.data.remote.model.ResponseWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlacesApiService {
    @GET("nearby")
    suspend fun getNearbyPlaces(
        @Query("ll") ll: String?,
        @Query("limit") limit: Int? = 10,
    ): Response<ResponseWrapper>

    @GET("{fsq_id}")
    suspend fun getPlaceDetails(
        @Path("fsq_id") fsqId: String?,
        @Query("fields") limit: String? = "fsq_id,categories,name,description,tel,rating,location",
    ): Response<Details>
}