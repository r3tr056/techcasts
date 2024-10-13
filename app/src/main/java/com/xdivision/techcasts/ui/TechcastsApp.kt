package com.xdivision.techcasts.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import kotlinx.coroutines.flow.StateFlow

import com.xdivision.techcasts.R
import com.xdivision.techcasts.ui.home.Home
import com.xdivision.techcasts.ui.player.PlayerScreen
import com.xdivision.techcasts.ui.player.PlayerViewModel
import com.xdivision.techcasts.util.DevicePosture

@Composable
fun TechcastsApp(
    DevicePosture: StateFlow<DevicePosture>,
    appState: TechcastsAppState = rememberTechcastsAppState()
) {
    if(appState.isOnline) {
        NavHost(
            navController=appState.navController,
            startDestination=Screens.Home.route
        ) {
            composable(Screens.Home.route) { backStackEntry ->
                Home(
                    navigateToPlayer={episodeUri ->
                        appState.navigateToPlayer(episodeUri, backStackEntry)
                    }
                )
            }

            composable(Screens.AudioPlayer.route) { backStackEntry ->
                val audioPlayerViewModel: AudioPlayerViewModel = viewModel(
                    factory=AudioPlayerViewModel.provideFactory(
                        owner=backStackEntry,
                        defaultArgs=backStackEntry.arguments
                    )
                )
                AudioPlayerScreen(audioPlayerViewModel, devicePosture, onBackPress=appState::navigateBack)
            }
        }
    } else {
        OfflineDialog { appState.refreshOnline() }
    }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text=stringResource(R.string.connection_error_title)) },
        text = { Text(text=stringResource(R.string.connecting_error_message)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry_label))
            }
        }
    )
}
