package com.xdivision.techcasts.ui.screens

sealed class Screens(val route: String) {
    object HomeScreen : Screens("homeScreen")
    object AudioPlayer : Screens("audioPlayer/{episodeUri}") {
        fun createRoute(episodeUri: String) = "audioPlayer/$episodeUri"
    }
    object PlayListScreen : Screens("playlistScreen")
}