package io.jacob.igozogo.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.R
import io.jacob.igozogo.ui.theme.IgozogoTheme
import io.jacob.igozogo.ui.tooling.DevicePreviews

@Composable
fun TitleTextItem(
    modifier: Modifier = Modifier,
    text: String,
    onMore: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.headlineMedium
        )

        onMore?.let {
            TextButton(
                onClick = it,
            ) {
                Text(text = stringResource(R.string.more))
            }
        }
    }
}

@DevicePreviews
@Composable
private fun TitleTextItemPreview() {
    IgozogoTheme {
        TitleTextItem(
            text = "Preview",
            onMore = {}
        )
    }
}