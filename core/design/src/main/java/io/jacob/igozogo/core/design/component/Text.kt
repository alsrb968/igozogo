package io.jacob.igozogo.core.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.R
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews

@Composable
fun TitleTextItem(
    modifier: Modifier = Modifier,
    text: String,
    onMore: (() -> Unit)? = null,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
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

        content()
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