package com.lee.crowdtracker.feature.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.lee.crowdtracker.libray.navermap.componenrt.NaverMapView

@Composable
fun HomeRoute(
    onShowSnackBar: suspend (String, String?) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen()
}

@Composable
internal fun HomeScreen() {
    NaverMapView()
}