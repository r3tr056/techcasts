package com.xdivision.techcasts.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
fun SearchBar(
    searchQuery: String,
    modfier: Modifier = Modifier,
    onSearchQueryChanged: (String) -> Unit
) {
    val contentColor = MaterialTheme.colors.onTertiaryContainer
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val showClearButton by remember { mutableStateOf(false) }

    Surface(
        modfier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colors.surface,
        contentColor = contentColor,
        tonalElevation = 4.dp
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChanged = onSearchQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(SEARCH_BAR_TAG
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
            textStyle = MaterialTheme.typography.titleMedium.copy(color = contentColor),
            singleLine = true,
            maxLines = 1,
            label = {
                Text(
                    text = stringResource(R.string.search_here),
                    color = contentColor.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id=R.drawable.ic_search),
                    modifier = Modifier.padding(start=8.dp)
                    contentDescription = stringResource(R.string_here)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotBalnk() && searchQuery.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .testTag(SEARCH_BAR_TAG)
                            .clickable {
                                onSearchQueryChanged("")
                                focusManager.clearFocus()
                            }
                    )
                }
            },
            colors = TextFieldDefaults.OutlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    TechcastsTheme {
        SearchBar(
            searchQuery = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            onSearchQueryChanged = {}
        )
    }
}