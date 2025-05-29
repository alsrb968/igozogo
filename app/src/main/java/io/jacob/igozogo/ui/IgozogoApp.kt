package io.jacob.igozogo.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.jacob.igozogo.R
import io.jacob.igozogo.feature.main.MainScreen
import io.jacob.igozogo.ui.theme.IgozogoTheme
import io.jacob.igozogo.ui.tooling.DevicePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IgozogoApp(
    modifier: Modifier = Modifier,
    appState: IgozogoAppState = rememberIgozogoAppState()
) {
    if (appState.isOnline) {
        MainScreen(
            modifier = modifier,
            appState = appState
        )
    } else {
        OfflineDialog { appState.refreshOnline() }
    }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.network_error_title)) },
        text = { Text(text = stringResource(R.string.network_error_message)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    )
}

@DevicePreviews
@Composable
private fun OfflineDialogPreview() {
    IgozogoTheme {
        OfflineDialog(onRetry = {})
    }
}