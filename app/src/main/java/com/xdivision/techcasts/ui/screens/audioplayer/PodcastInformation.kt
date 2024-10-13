

@Composable
private fun PodcastInformation(
    title: String,
    name: String,
    summary: String,
    titleTextStyle: TextStyle = MaterialTheme.typography.h5,
    nameTextStyle: TextStyle = MaterialTheme.typography.h3,
) {
    Column(
        horizontalAlignment=Alignment.CenterHorizontally,
        modifer=Modifier.padding(horizontal=8.dp)
    ) {
        Text(
            text=name,
            style=nameTextStyle,
            maxLines=1,
            overflow=TextOverflow.Ellipsis
        )
        Spacer(modifer=Modifier.height(32.dp))
        Text(
            text = title,
            style = titleTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(32.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = summary,
                style = MaterialTheme.typography.body2,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}