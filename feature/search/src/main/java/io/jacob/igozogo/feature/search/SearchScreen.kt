package io.jacob.igozogo.feature.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    SearchScreen(
        modifier = modifier,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter),
            query = "",
            onQueryChange = {},
            onSearch = {},
            onFocusChanged = {}
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = "Search"
        )
    }
}


@OptIn(FlowPreview::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit
) {
    val searchFlow = remember { MutableStateFlow(query) }

    LaunchedEffect(Unit) {
        searchFlow
            .debounce(500)
            .collectLatest { text ->
                onSearch(text)
            }
    }

    TextField(
        value = query,
        onValueChange = {
            onQueryChange(it)
            searchFlow.value = it
        },
        placeholder = { Text("Search...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = {
                    onQueryChange("")
                    searchFlow.value = ""
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .onFocusChanged { onFocusChanged(it.isFocused) }
    )
}

@DevicePreviews
@Composable
private fun SearchBarPreview() {
    IgozogoTheme {
        SearchBar(
            query = "Search query",
            onQueryChange = {},
            onSearch = {},
            onFocusChanged = {}
        )
    }
}