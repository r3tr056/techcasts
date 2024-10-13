package com.xdivision.techcasts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker.Companion.getOrCreate
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import com.xdivision.techcasts.ui.theme.TechcastsTheme
import com.xdivision.techcasts.util.DevicePosture
import com.xdivision.techcasts.util.isBookPosture
import com.xdivision.techcasts.util.isSeparatingPosture
import com.xdivision.techcasts.util.isTableTopPosture

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fit within system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Device posture flow, emitted when the windowLayoutInfo changes
        val devicePosture = getOrCreate(this).windowLayoutInfo(this)
            .flowWithLifecycle(this.lifecycle)
            .map { layoutInfo ->
                val foldingFeature = layoutInfo.displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
                when {
                    isTableTopPosture(foldingFeature) -> DevicePosture.TableTopPosture(foldingFeature.bounds)
                    isBookPosture(foldingFeature) -> DevicePosture.TableTopPosture(foldingFeature.bounds)
                    isSeperatingPosture(foldingFeature) -> DevicePosture.SeperatingPosture(foldingFeature.bounds, foldingFeature.orientation)
                    else -> DevicePosture.NormalPosture
                }
            }
            .stateIn(
                scope=lifecycleScope,
                started=SharingStarted.Eagerly,
                initialValue=DevicePosture.NormalPosture
            )

        setContent {
            TechcastsTheme {
                TechcastsApp(devicePosture)
            }
        }
    }
}
