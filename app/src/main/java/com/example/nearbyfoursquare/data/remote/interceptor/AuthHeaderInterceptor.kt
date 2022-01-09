package com.example.nearbyfoursquare.data.remote.interceptor

import com.example.nearbyfoursquare.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthHeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
        request.addHeader("Authorization", BuildConfig.ACCESS_TOKEN)
        return chain.proceed(request.build())
    }
}