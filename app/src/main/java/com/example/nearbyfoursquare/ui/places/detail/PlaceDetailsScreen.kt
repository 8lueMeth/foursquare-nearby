package com.example.nearbyfoursquare.ui.places.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.nearbyfoursquare.R
import com.example.nearbyfoursquare.data.local.entity.PlaceDetailsEntity
import com.example.nearbyfoursquare.ui.component.Loading
import com.example.nearbyfoursquare.ui.component.errorToast
import com.example.nearbyfoursquare.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@Composable
fun PlaceDetailsScreen(placeDetailsViewModel: PlaceDetailsViewModel) {
    val place = placeDetailsViewModel.place?.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.place_details_app_bar_title)) },
                elevation = 0.dp,
            )
        },
    ) {
        PlaceDetailsSection(viewModel = placeDetailsViewModel, placeResult = place?.value)
    }
}

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@Composable
fun PlaceDetailsSection(
    viewModel: PlaceDetailsViewModel,
    placeResult: Resource<PlaceDetailsEntity>?
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        placeResult.apply {
            if ((placeResult is Resource.Success || placeResult is Resource.Error || placeResult is Resource.Loading) && placeResult.data != null) {
                Card(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    elevation = 4.dp,
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = placeResult.data,
                            builder = {
                                crossfade(true)
                                transformations(CircleCropTransformation())
                            },
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = placeResult.data.name,
                    textAlign = TextAlign.Center
                )
                if (placeResult.data.description != null) {
                    Row(modifier = Modifier.padding(top = 16.dp)) {
                        Icon(
                            modifier = Modifier.padding(end = 8.dp),
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                        Text(text = placeResult.data.description, textAlign = TextAlign.Center)
                    }
                }
                if (placeResult.data.address != null) {
                    Row(modifier = Modifier.padding(top = 16.dp)) {
                        Icon(
                            modifier = Modifier.padding(end = 8.dp),
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null
                        )
                        Text(text = placeResult.data.address, textAlign = TextAlign.Center)
                    }
                }
                if (placeResult.data.tel != null) {
                    Row(modifier = Modifier.padding(top = 16.dp)) {
                        Icon(
                            modifier = Modifier.padding(end = 8.dp),
                            imageVector = Icons.Default.Phone,
                            contentDescription = null
                        )
                        Text(text = placeResult.data.tel, textAlign = TextAlign.Center)
                    }
                }
                if (placeResult.data.rating != null) {
                    Row(modifier = Modifier.padding(top = 16.dp)) {
                        Icon(
                            modifier = Modifier.padding(end = 8.dp),
                            imageVector = Icons.Default.Star,
                            contentDescription = null
                        )
                        Text(text = "${placeResult.data.rating}", textAlign = TextAlign.Center)
                    }
                }
                if (placeResult is Resource.Error) {
                    Error(onRetryClick = { viewModel.refreshPlaceDetails() })
                    errorToast(context)
                }
                if (placeResult is Resource.Loading) {
                    Loading()
                }
            } else if (placeResult is Resource.Error && placeResult.data == null) {
                Error(onRetryClick = { viewModel.refreshPlaceDetails() })
                errorToast(context)
            } else if (placeResult is Resource.Loading) {
                Loading()
            }
        }
    }
}

@Composable
fun Error(onRetryClick: () -> Unit) {
    IconButton(
        onClick = { onRetryClick() },
        modifier = Modifier
            .clip(CircleShape)
            .padding(top = 16.dp)
    ) {
        Icon(
            Icons.Default.Refresh,
            contentDescription = null,
        )
    }
}