

@Composable
fun rememberDominantColorState(
    context: Context = LocalContext.current,
    defaultColor: Color = MaterialTheme.colors.primary,
    defaultOnColor: Color = MaterialTheme.colors.onPrimary,
    cacheSize: Int = 12,
    isColorValid: (Color) -> Boolean = { true }
): DominantColorState = remember { DominantColorState(context, defaultColor, defaultOnColor, cacheSize, isColorValid )
}

// A composable which allows dynamic theming of the color of an image
@Composable
fun DynamicThemePrimaryColorsFromImage(
    dominantColorState: DominantColorState = rememberDominantColorState(),
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colors.copy(
        primary=animateColorAsState(
            dominantColorState.color,
            sprint(stiffness=Spring.StiffnessLow)
        ).value,
        onPrimary=animateColorAsState(
            dominantColorState.onColor,
            sprint(stiffness=Spring.StiffnessLow)
        ).value
    )
    MaterialTheme(colors=colors, content=content)
}

// holds dominant colors for dynamic theming
@Immutable
private data class DominantColors(val color: Color, val onColor: Color)

// A class that stores and caches the result of any calculated 
// dominant colors from images.
@Stable
class DominantColorState(
    private val context: Context,
    private val defaultColor: Color,
    private val defaultOnColor: Color,
    cacheSize: Int = 12,
    private val isColorValid: (Color) -> Boolean = { true }
) {
    var color by mutableStateOf(defaultColor)
        private set

    var onColor by mutableStateOf(defaultOnColor)
        private set

    private val cache = when {
        cacheSize > 0 -> LruCache<String, DominantColors>(cacheSize)
        else -> null
    }

    suspend fun updateColorsFromImageUrl(url: String) {
        val res = calculateDominantColor(url)
        color = result?.color ?: defaultColor
        onColor = result?.onColor ?: defaultOnColor
    }

    private suspend calculateDominantColor(url: String): DominantColors? {
        val cached = cache?.get(url)
        if (cached != null) {
            return cached
        }

        return calculateSwatchesInImage(context, url)
            .sortedByDescending { swatch -> swatch.populating }
            .firstOrNull { swatch -> isColorValid(Color(swatch.rgb)) }
            ?.let {
                swatch -> DominantColors(
                    color=Color(swatch.rgb),
                    onColor=Color(swatch.bodyTextColor).copy(alpha=1f)
                )
            }?.also { result -> cache?.put(url, result) }
    }

    //Reset to default
    fun reset() {
        color = defaultColor
        onColor = defaultOnColor
    }
}

private suspend fun calculateSwatchesInImage(context: Context, imageUrl: String) : List<Palette.Swatch> {
    val req = ImageRequest.Builder(context)
        .data(imageUrl)
        .size(128).scale(Scale.FILL)
        .allowHardware(false)
        .memoryCacheKey("$imageUrl.palette")
        .build()

    val bitmap = when (val result = context.imageLoader.execute(request)) {
        is SuccessResult -> result.drawable.toBitmap()
        else -> null
    }

    val bitmap?.let {
        withContext(Dispatchers.Default) {
            val palette = Palette.Builder(bitmap)
                .resizeBitmapArea(0)
                .clearFilters()
                .maximumColorCount(8)
                .generate()

            palette.swatches
        }
    }?: emptyList()
}