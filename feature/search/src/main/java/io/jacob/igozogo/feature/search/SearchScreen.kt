package io.jacob.igozogo.feature.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    SearchScreen(
        modifier = modifier,
        onShowSnackbar = onShowSnackbar
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        SearchBar(
            modifier = Modifier,
            query = "",
            onQueryChange = {},
            onSearch = {},
            onFocusChanged = {}
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

//    LaunchedEffect(Unit) {
//        searchFlow
//            .debounce(500)
//            .collectLatest { text ->
//                onSearch(text)
//            }
//    }

    TextField(
        value = query,
        onValueChange = {
            onQueryChange(it)
            searchFlow.value = it
        },
        placeholder = { Text("Search...") },
        leadingIcon = {
            Icon(
                imageVector = IgozogoIcons.Search,
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
                        imageVector = IgozogoIcons.Close,
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