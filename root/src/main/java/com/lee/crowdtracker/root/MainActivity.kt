package com.lee.crowdtracker.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lee.crowdtracker.libray.design.theme.CDTheme
import com.lee.crowdtracker.root.ui.CrowdTrackerApp
import com.lee.crowdtracker.root.ui.rememberCrowdTrackerAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initNaverMapSdk()
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val appState = rememberCrowdTrackerAppState(
                snackbarHostState = snackbarHostState
            )
            CDTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    CrowdTrackerApp(
                        modifier = Modifier.padding(innerPadding),
                        snackbarHostState = snackbarHostState,
                        appState = appState
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.downloadArea()
    }
}