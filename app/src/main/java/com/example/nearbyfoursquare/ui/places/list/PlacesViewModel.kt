package com.example.nearbyfoursquare.ui.places.list

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.nearbyfoursquare.data.remote.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
) : ViewModel() {

    private var location: Location? = null
    private var latLong = ""

    private val refreshTriggerChannel = Channel<String>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    val places = refreshTrigger.flatMapLatest { placesRepository.getPlaces(it) }.asLiveData()

    fun refreshPlaces() = refreshTriggerChannel.trySend(latLong)

    fun setCurrentLocation(location: Location) {
        if (this.location == null) {
            this.location = location
            latLong = "${location.latitude},${location.longitude}"
            refreshTriggerChannel.trySend(latLong)
        } else if (location.distanceTo(this.location) > 100) {
            this.location = location
            latLong = "${location.latitude},${location.longitude}"
            refreshTriggerChannel.trySend(latLong)
        }
    }
}