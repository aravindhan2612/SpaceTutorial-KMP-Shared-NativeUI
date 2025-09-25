package com.jetbrains

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetbrains.spacetutorial.RocketLaunchViewModel
import com.jetbrains.spacetutorial.theme.AppTheme
import com.jetbrains.spacetutorial.theme.app_theme_successful
import com.jetbrains.spacetutorial.theme.app_theme_unsuccessful
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

import spacetutorial.composeapp.generated.resources.Res
import spacetutorial.composeapp.generated.resources.compose_multiplatform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val viewModel = koinViewModel<RocketLaunchViewModel>()
    val state by remember { viewModel.state }
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "SpaceX Launches",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                )
            }
        ) { padding ->
            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    coroutineScope.launch {
                        viewModel.loadLaunches()
                        isRefreshing = false
                    }
                }
            ) {
                if (state.isLoading && !isRefreshing) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn {
                        items(state.rocketLaunches) { launch ->
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "${launch.missionName} - ${launch.launchYear}",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = if (launch.launchSuccess == true) "Successfull" else "Unsuccessfull",
                                    color = if (launch.launchSuccess == true) app_theme_successful else app_theme_unsuccessful
                                )
                                Spacer(Modifier.height(8.dp))
                                launch.details?.let { details ->
                                    if (details.isNotBlank()) {
                                        Text(
                                            text = details
                                        )
                                    }
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }

            }
        }
    }
}