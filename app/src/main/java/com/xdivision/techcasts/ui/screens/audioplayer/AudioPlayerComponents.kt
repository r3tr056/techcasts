package com.xdivision.techcasts.ui.screens.audioplayer

import androidx.compose.runtime.Composable
import com.xdivision.techcasts.ui.components.FullScreenLoading

@Composable
private fun AudioPlayerSlider(
    episodeDuration: Duration?,
    val sliderValue: Float = 0f,
    val timePassed: Duration?,
    val timeLeft: Duration?,
    val onValueChange:(Duration) -> Unit = {}
) {
    val timePassedFormatted
        get() = timePassed.formatDuration()

    val timeLeftFormatted
        get() = timeLeft.formatDuration()
}

@Composable
private fun AudioPlayerImage(
    podcastImageUrl: String,
    modifer: Modifier=Modifier
) {
    AsyncImage(
        model=ImageRequest.Builder(LocalContext.current)
            .data(podcastImageUrl)
            .crossfade(true)
            .build(),
        contentDescription=null,
        contentScale=ContentScale.Crop,
        modifier = modifier
            .sizeIn(maxWidth=500.dp, maxHeight=500.dp)
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium)
    )
}

@Composable
private fun AudioPlayerSlider(episodeDuration: Duration?) {
    if (episodeDuration != null) {
        Column(Modifier.fillMaxWidth()) {
            Slider(value = 0f, onValueChange = {})
            Row(Modifier.fillMaxWidth()) {
                Text(text="0s")
                Spacer(modifier=Modifier.weight(1f))
                Text("${episodeDuration.seconds}s")
            }
        }
    }
}

@Composable
private fun AudioPlayerButtons(
    modifier: Modifier=Modifier,
    playerButtonSize: Dp = 72.dp,
    sideButtonSize: Dp = 48.dp
) {
    Row(
        modifier=modifer.fillMaxWidth(),
        verticalAlignment=Alignment.CenterVerically,
        horizontalAlignment=Arrangement.SpaceEvenly
    ) {
        val buttonsModifier = Modifier
            .size(sideButtonSize)
            .semantics { rolde = Role.Button }
        // Skip previous button
        Image(
            imageVector=Icons.Filled.SkipPrevious,
            contentDescription=stringResource(R.string.cd_skip_previous),
            contentScale=ContentScale.Fit,
            colorFilter=ColorFilter.tint(LocalContentColor.current),
            modifer=buttonsModifier
        )
        // Replay button
        Image(
            imageVector=Icons.Filled.Replay10,
            contentDescription=stringResource(R.string.cd_replay10),
            contentScale=ContentScale.Fit,
            colorFilter=ColorFilter.tint(LocalContentColor.current),
            modifer=buttonsModifier
        )
    }
}

// Stateful version of the player
@Composable
fun AudioPlayerScreen(
    viewModel: AudioPlayerViewModel,
    devicePosture: StateFlow<DevicePosture>,
    onBackPress: () -> Unit
) {
    val uiState = viewModel.uiState
    val devicePostureValue by devicePosture.collectAsState()
    AudioPlayerScreen(uiState, devicePostureValue, onBackPress)
}

// State less version of the player
@Composable
private fun AudioPlayerScreen(
    uiState: AudioPlayerUiState,
    devicePosture: DevicePosture,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier) {
        if (uiState.podcastName.isNotEmpty()) {
            AudioPlayerContent(uiState, devicePosture, onBackPress)
        } else {
            FullScreenLoading(modifier)
        }
    }
}

@Composable
fun AudioPlayerContent(
    uiState: AudioPlayerUiState,
    devicePosture: DevicePosture,
    onBackPress: () -> Unit
) {
    PlayerDynamicTheme(uiState.podcastImageUrl) {
        when (devicePosture) {
            is DevicePosture.TableTopPosture -> AudioPlayerContentTableTop(uiState, devicePosture, onBackPress)
            is DevicePosture.BookPosture -> AudioPlayerContentBook(uiState, devicePosture, onBackPress)
            is DevicePosture.SeperatingPosture ->
                if (devicePosture.orientation == FoldingFeature.Orientation.HORIZONTAL) {
                    AudioPlayerContentTableTop(uiState, DevicePosture.TableTopPosture(devicePosture.hingePosition), onBackPress)
                } else {
                    AudioPlayerContentBook(
                        uiState,
                        DevicePosture.BookPosture(devicePosture.hingePosition),
                        onBackPress
                    )
                }
            else -> AudioPlayerContentRegular(uiState, onBackPress)
        }
    }
}

@Composable
private fun AudioPlayerContentRegular(
    uiState: AudioPlayerUiState,
    onBackPress: () -> Unit
) {
    Column(
        modifier=Modifier
            .fillMaxSize()
            .verticalGradientScrim(
                color=MaterialTheme.colors.primary.copy(alpha=0.50f),
                startYPercentage=1f,
                endYPercentage=0f
            )
            .systemBarsPadding()
            .padding(horizontal=8.dp)
    ) {
        TopAppBar(onBackPress=onBackPress)
        Column(
            horizontalAlignment=Alignment.CenterHorizontally,
            modifier=Modifier.padding(horizontal=8.dp)
        ) {
            Spacer(modifier=Modifier.weight(1f))
            PlayerImage(
                podcastImageUrl=uiState.podcastImageUrl,
                modifier=Modifier.weight(10f)
            )
            Spacer(modifier=Modifier.height(32.dp))
            PodcastDescription(uiState.title, uiState.pocastName)
            Spacer(modifier=Modifier.height(32.dp))
            Column(
                horizontalAlignment=Alignment.CenterHorizontally,
                modifer=Modifier.weight(10f)
            ) {
                AudioPlayerSlider(uiState.duration)
                AudioPlayerButtons(Modifier.padding(veritcal=8.dp))
            }
            Spacer(modifer=Modifier.weight(1f))
        }
    }
}

@Composable
private fun AudioPlayerContentTableTop(
    uiState: AudioPlayerUiState,
    tableTopPosture: DevicePosture.TableTopPosture,
    onBackPress: () -> Unit
) {
    val hingePosition = with(LocalDensity.current) { tableTopPosture.hingePosition.top.toDp() }
    val hingeHeight = with(LocalDensity.current) { tableTopPosture.hingePosition.height().toDp() }

    Column(modifier = Modifier.fillMaxSize()) {
        // Content for the top part of the screen
        Column(
            modifier = Modifier
                .height(hingePosition)
                .fillMaxWidth()
                .verticalGradientScrim(
                    color = MaterialTheme.colors.primary.copy(alpha=0.50f),
                    startYPercentage = 1f,
                    endYPercentage = 0f
                )
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(
                        WindowInsetsSides.Horizontal + WindowInsetsSides.Top
                    )
                ).padding(32.dp)
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AudioPlayerImage(uiState.podcastImageUrl)
        }

        // Space for the hinge
        Spacer(modifer=Modifier.height(hingeHeight))

        // Content for the table part of the screen
        Column(
            modifier=Modifier
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(
                        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                    )
                ).padding(horizontal=32.dp, veritcal=8.dp),
            horizontalAlignment=Alignment.CenterHorizontally
        ) {

        }
    }
}