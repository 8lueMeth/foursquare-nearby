package com.example.nearbyfoursquare.ui.places.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.nearbyfoursquare.data.remote.repository.PlacesRepository
import com.example.nearbyfoursquare.util.Refresh
import com.example.nearbyfoursquare.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val placesRepository: PlacesRepository,
) : ViewModel() {

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    init {
        viewModelScope.launch { refreshTriggerChannel.send(Refresh.NORMAL) }
    }

    val place = savedStateHandle.get<String>("placeId")?.let { id ->
        refreshTrigger.flatMapLatest { placesRepository.getPlaceDetails(id) }
    }?.asLiveData()

    fun refreshPlaceDetails() {
        if (place?.value !is Resource.Loading) {
            viewModelScope.launch { refreshTriggerChannel.send(Refresh.FORCE) }
        }
    }
}