package com.xdivision.techcasts.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment

// Full-screen circular progress indicator
@Composable
private fun FullScreenLoading(modifier: Modifier=Modifier) {
    Box(
        modifier=modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}