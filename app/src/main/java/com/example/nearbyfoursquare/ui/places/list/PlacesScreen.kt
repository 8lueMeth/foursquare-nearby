package com.example.nearbyfoursquare.ui.places.list

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.ExperimentalPagingApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.nearbyfoursquare.R
import com.example.nearbyfoursquare.data.local.entity.PlaceEntity
import com.example.nearbyfoursquare.ui.component.Loading
import com.example.nearbyfoursquare.ui.component.errorToast
import com.example.nearbyfoursquare.util.Resource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi


@ObsoleteCoroutinesApi
@SuppressLint("MissingPermission")
@ExperimentalPermissionsApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalPagingApi
@Composable
fun PlacesScreen(viewModel: PlacesViewModel, navigateToPlaceDetailsScreen: (String) -> Unit) {
    val context = LocalContext.current

    var isLocationEnabled by remember { mutableStateOf(false) }
    var allPermissionsGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationManager = getSystemService(context, LocationManager::class.java)
    isLocationEnabled =
        locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true || locationManager?.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        ) == true

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest().setInterval(20000).setFastestInterval(10000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setSmallestDisplacement(100f)
    LaunchedEffect(key1 = isLocationEnabled, key2 = allPermissionsGranted, block = {
        if (isLocationEnabled && allPermissionsGranted) {
            Looper.myLooper()?.let {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            for (location in locationResult.locations) {
                                viewModel.setCurrentLocation(location)
                            }
                        }
                    },
                    it
                )
            }
        } else if (!isLocationEnabled && allPermissionsGranted) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(context, intent, null)
        }
    })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.places_app_bar_title)) },
                elevation = 0.dp,
            )
        },
    ) {
        Permission(
            viewModel = viewModel,
            context = context,
            isLocationEnabled = isLocationEnabled,
            allPermissionsGranted = { allPermissionsGranted = true },
            navigateToPlaceDetailsScreen = navigateToPlaceDetailsScreen
        )
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ObsoleteCoroutinesApi
@ExperimentalPagingApi
@ExperimentalPermissionsApi
@Composable
fun Permission(
    viewModel: PlacesViewModel,
    context: Context,
    isLocationEnabled: Boolean,
    allPermissionsGranted: () -> Unit,
    navigateToPlaceDetailsScreen: (String) -> Unit,
) {
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    val locationPermissions = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )

    LaunchedEffect(key1 = locationPermissions.allPermissionsGranted, block = {
        if (locationPermissions.allPermissionsGranted) allPermissionsGranted()
    })

    PermissionsRequired(
        multiplePermissionsState = locationPermissions,
        permissionsNotGrantedContent = {
            if (doNotShowRationale) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(id = R.string.location_permission_error),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Rationale(
                    onDoNotShowRationale = { doNotShowRationale = true },
                    onRequestPermission = { locationPermissions.launchMultiplePermissionRequest() }
                )
            }
        },
        permissionsNotAvailableContent = {
            PermissionDenied {
                startActivity(
                    context,
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts(
                            "package",
                            context.packageName,
                            null
                        )
                    ),
                    null
                )
            }
        }
    ) {
        if (!isLocationEnabled) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.turn_on_gps),
                    textAlign = TextAlign.Center
                )
            }

        }
        val places = viewModel.places.observeAsState()
        Places(
            placesResult = places.value,
            viewModel = viewModel,
            onItemClicked = { navigateToPlaceDetailsScreen(it) })
    }
}

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@ExperimentalPagingApi
@ExperimentalMaterialApi
@Composable
fun Places(
    placesResult: Resource<List<PlaceEntity>>?,
    viewModel: PlacesViewModel,
    onItemClicked: (id: String) -> Unit,
) {
    val context = LocalContext.current
    Box {
        placesResult.apply {
            if (placesResult is Resource.Error && placesResult.data == null) {
                Error(onRetryClick = { viewModel.refreshPlaces() })
                errorToast(context)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                    top = 8.dp
                ),
            ) {
                if (placesResult is Resource.Error) {
                    item { Error(onRetryClick = { viewModel.refreshPlaces() }) }
                    errorToast(context)
                }

                if ((placesResult is Resource.Success || placesResult is Resource.Error || placesResult is Resource.Loading) && placesResult.data != null) {
                    items(items = placesResult.data) { item ->
                        PlaceRow(place = item, onItemClicked)
                    }
                }
            }
            if (placesResult is Resource.Loading) {
                Loading()
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun PlaceRow(place: PlaceEntity?, onItemClicked: (id: String) -> Unit) {
    place ?: return
    Row(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            onClick = { onItemClicked(place.id) }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
            ) {
                Card(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    elevation = 4.dp,
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = place.categoryIcon,
                            builder = {
                                crossfade(true)
                                transformations(CircleCropTransformation())
                            },
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = place.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(modifier = Modifier.padding(top = 16.dp)) {
                        Icon(
                            modifier = Modifier.padding(end = 4.dp),
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                        )
                        Text(text = stringResource(id = R.string.places_distance, place.distance))
                    }
                }
            }
        }
    }
}

@Composable
fun Error(onRetryClick: () -> Unit) {
    IconButton(
        onClick = { onRetryClick() },
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .padding(top = 16.dp)
    ) {
        Icon(
            Icons.Default.Refresh,
            contentDescription = null,
        )
    }
}

@Composable
private fun Rationale(
    onDoNotShowRationale: () -> Unit,
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(id = R.string.location_permission_rationale),
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = onRequestPermission) {
                Text(
                    stringResource(id = R.string.location_permission_request_permission),
                    textAlign = TextAlign.Center
                )
            }
            Button(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                onClick = onDoNotShowRationale
            ) {
                Text(
                    stringResource(id = R.string.location_permission_do_not_show_rationale),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PermissionDenied(
    navigateToSettingsScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(id = R.string.location_permission_permission_denied),
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = navigateToSettingsScreen
        ) {
            Text(stringResource(id = R.string.location_permission_open_settings))
        }
    }
}