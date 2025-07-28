package io.jacob.igozogo.feature.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var isShowBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter),
            query = "",
            onQueryChange = {},
            onSearch = {},
            onFocusChanged = {}
        )

        Button(
            modifier = Modifier
                .align(Alignment.Center),
            onClick = { isShowBottomSheet = !isShowBottomSheet }
        ) {
            Text(text = "Search")
        }
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        if (isShowBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                dragHandle = null,
//                containerColor = Color.Transparent,
//                scrimColor = Color.Transparent,
                onDismissRequest = { isShowBottomSheet = false },
            ) {
                Spacer(modifier = Modifier.height(300.dp))

                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = "Bottom Sheet Content"
                )

                Spacer(modifier = Modifier.height(300.dp))
            }
        }
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