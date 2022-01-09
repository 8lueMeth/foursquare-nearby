package com.example.nearbyfoursquare.data.remote.model

data class Photo(
    val prefix: String,
    val suffix: String,
    val width: Int,
    val height: Int,
) {
    fun getImage() = this.prefix + "400x400" + this.suffix
}