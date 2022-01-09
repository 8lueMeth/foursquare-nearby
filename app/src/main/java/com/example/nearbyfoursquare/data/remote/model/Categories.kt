package com.example.nearbyfoursquare.data.remote.model

data class Categories(
    val id: Int,
    val name: String,
    val icon: Icon
) {
    fun getIcon() = icon.prefix + "120" + icon.suffix
}