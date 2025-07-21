package com.lee.crowdtracker.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lee.crowdtracker.libray.design.theme.CDTheme
import com.lee.crowdtracker.root.ui.CrowdTrackerApp
import com.lee.crowdtracker.root.ui.rememberCrowdTrackerAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberCrowdTrackerAppState()
            CDTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CrowdTrackerApp(
                        modifier = Modifier.padding(innerPadding),
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CDTheme {
        Greeting("Android")
    }
}