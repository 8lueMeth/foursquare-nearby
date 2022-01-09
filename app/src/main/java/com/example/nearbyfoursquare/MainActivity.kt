package com.example.nearbyfoursquare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import com.example.nearbyfoursquare.ui.Screens
import com.example.nearbyfoursquare.ui.places.detail.PlaceDetailsScreen
import com.example.nearbyfoursquare.ui.places.detail.PlaceDetailsViewModel
import com.example.nearbyfoursquare.ui.places.list.PlacesScreen
import com.example.nearbyfoursquare.ui.places.list.PlacesViewModel
import com.example.nearbyfoursquare.ui.theme.NearbyFoursquareTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@ExperimentalPagingApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NearbyFoursquareTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.Places.route,
                    builder = {
                        addPlacesScreen(navController = navController)
                        addPlaceDetailsScreen(navController = navController)
                    }
                )
            }
        }
    }
}

@ObsoleteCoroutinesApi
@ExperimentalPermissionsApi
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
fun NavGraphBuilder.addPlacesScreen(
    navController: NavHostController,
) {
    composable(
        route = Screens.Places.route,
    ) {
        val viewModel: PlacesViewModel = hiltViewModel()
        PlacesScreen(
            viewModel = viewModel,
            navigateToPlaceDetailsScreen = { placeId ->
                navController.navigate("${Screens.Details.route}/$placeId")
            },
        )
    }
}

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
fun NavGraphBuilder.addPlaceDetailsScreen(
    navController: NavHostController,
) {
    composable(
        route = Screens.Details.route + "/{placeId}",
        arguments = Screens.Details.arguments,
    ) {
        val viewModel: PlaceDetailsViewModel = hiltViewModel()
        PlaceDetailsScreen(placeDetailsViewModel = viewModel)
    }
}