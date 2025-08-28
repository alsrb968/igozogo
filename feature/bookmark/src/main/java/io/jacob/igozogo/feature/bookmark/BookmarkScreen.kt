package io.jacob.igozogo.feature.bookmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.component.IgozogoScaffold
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import io.jacob.igozogo.core.design.R as designR

@Composable
fun BookmarkRoute(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    BookmarkScreen(
        modifier = modifier,
        onShowSnackbar = onShowSnackbar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val event = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    LaunchedEffect(event) {
        val snackBarResult = onShowSnackbar("Bookmark", "Clicked")
        Timber.i("Snackbar result: $snackBarResult")
    }

    IgozogoScaffold(
        modifier = modifier,
        title = stringResource(designR.string.core_design_bookmark)
    ) { paddingValues, nestedScrollConnection ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        event.tryEmit(Unit)
                    },
                painter = painterResource(designR.drawable.core_design_img_empty_bookmark),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "No Bookmarks Yet",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You can bookmark your favorite places here.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }

}

@DevicePreviews
@Composable
private fun BookmarkScreenPreview() {
    IgozogoTheme {
        BookmarkScreen(
            onShowSnackbar = { _, _ -> true }
        )
    }
}