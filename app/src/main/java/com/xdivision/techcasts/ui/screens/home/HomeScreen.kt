import com.xdivision.techcasts.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

import com.xdivision.techcasts.R
import com.xdivision.techcasts.data.models.local.Music
import com.xdivision.techcasts.ui.components.MusicItem
import com.xdivision.techcasts.ui.theme.MusicXTheme
import com.xdivision.techcasts.ui.utils.HOME_SCREEN_GREETING
import com.xdivision.techcasts.ui.utils.getMusicItemTag

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToPlayer: (String) -> Unit
) {
    val uiState by viewModel.uiState
    CollectEvents(event=viewModel.navigateToAudioPlayer, navigateToAudioPlayer)
    Surface(modifier=Modifier.fillMaxSize()) {
        HomeScreenContent(
            featuredPodacasts=uiState.featuredPodacasts,
            isRefreshing=uiState.refreshing,
            homeCategories=uiState.homeCategories,
            selectedHomeCategory=uiState.selectedHomeCategory,
            onCategorySelected=viewModel::onHomeCategorySelected,
            onPodcastUnfollowed=viewModel::onPodcastUnfollowed,
            navigateToPlayer=navigateToPlayer,
            modifier=Modifier.fillMaxSize()
        )
    }
}

@Composable
fun HomeScreenAppBar(
    backgroundColor: Color,
    modifier: Modifier=Modifier
) {
    TopAppBar(
        title = {
            Row {
                Image(
                    painter=painterResource(R.drawable.ic_logo),
                    contentDescription=null
                )
                Icon(
                    painter=painterResource(R.drawable.ic_text_logo),
                    contentDescription=stringResource(R.string.app_name),
                    modifier=Modifier
                        .padding(start=4.dp)
                        .heightIn(max=24.dp)
                )
            }
        },
        backgroundColor=backgroundColor,
        actions = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(
                    onClick = {} 
                ) {
                    Icon(
                        imageVector=Icons.Filled.Search,
                        contentDescription=stringResource(R.string.cd_search)
                    )
                }
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector=Icons.Default.AccountCircle,
                        contentDescription = stringResource(R.string.cd_account)
                    )
                }
            }
        },
        modifier=modifier
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(
    featuredPodacasts: List<PodcastWithExtraInfo>,
    isRefreshing: Boolean,
    selectedHomeCategory: HomeCategory,
    homeCategories: List<HomeCategory>,
    modifier: Modifier=Modifier,
    onPodcastUnfollowed: (String) -> Unit,
    onCategorySelected: (HomeCategory) -> Unit,
    navigateToAudioPlayer: (String) -> Unit
) {
    Column(
        modifier=modifier.windowInsetsPadding(
            WindowInsets.systemBars.only(WindowInsetsSlides.Horizontal)
        )
    ) {
        val surfaceColor = MaterialTheme.colors.surface
        val dominantColorState = rememberDominantColorState { color ->
            color.contrastAgainst(surfaceColor) >= MinContrastOfPrimaryVsSurface
        }

        DynamicThemePrimaryColorsFromImage(dominantColorState) {
            val pagerState = rememberPagerState()
            val selectedImageUrl = featuredPodcasts.getOrNull(pagerState.currentPage)?.podcast?.imageUrl

            LaunchedEffect(selectedImageUrl) {
                if (selectedImageUrl != null) {
                    dominantColorState.updateColorsFromImageUrl(selectedImageUrl)
                } else {
                    dominantColorState.reset()
                }
            }

            Column(
                modifier=Modifier
                    .fillMaxWidth()
                    .verticalGradientScrim(
                        color = MaterialTheme.colors.primary.copy(alpha=0.38f),
                        startYPercentage=1f,
                        endYPercentage=0f
                    )
            ) {
                val appBarColor = MaterialTheme.colors.surface.copy(alpha=0.87f)
            }
        }
    }
}

@Composable
private fun HomeScreenTabs(
    categories: List<HomeCategory>,
    selectedCategory: HomeCategory,
    onCategorySelected: (HomeCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        HomeScreenTabIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedIndex])
        )
    }

    TabRow(
        selectedTabIndex = selectedIndex,
        indicator = indicator,
        modifier = modifier
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategorySelected(category) },
                text = {
                    Text(
                        text = when (category) {
                            HomeCategory.Libary -> stringResource(R.string.home_libary)
                        }
                    )
                }
            )
        }
    }
}