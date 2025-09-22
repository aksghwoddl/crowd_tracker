package com.lee.crowdtracker.root

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lee.crowdtracker.libray.design.theme.CDTheme
import com.lee.crowdtracker.root.ui.CrowdTrackerApp
import com.lee.crowdtracker.root.ui.rememberCrowdTrackerAppState
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val permissionLauncher by lazy {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }) { // 모든 권한이 충족되면
                viewModel.onPermissionGranted()
            } else {
                Toast.makeText(this, "필수 권한을 설정 해주세요!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isInitialized.value.not()
        }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )

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

    override fun onStart() {
        super.onStart()
        viewModel.downloadArea()
    }
}