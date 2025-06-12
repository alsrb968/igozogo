package io.jacob.igozogo.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingRoute(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Setting")
    }
}