package io.jacob.igozogo.feature.bookmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.R
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber

@Composable
fun BookmarkRoute(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    BookmarkScreen(
        modifier = modifier,
        onShowSnackbar = onShowSnackbar,
    )
}

@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val event = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    LaunchedEffect(event) {
        val snackBarResult = onShowSnackbar("Bookmark", "Clicked")
        Timber.i("Snackbar result: $snackBarResult")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    event.tryEmit(Unit)
                },
            painter = painterResource(id = R.drawable.core_design_img_empty_bookmark),
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

@DevicePreviews
@Composable
private fun BookmarkScreenPreview() {
    IgozogoTheme {
        BookmarkScreen(
            onShowSnackbar = { _, _ -> true }
        )
    }
}