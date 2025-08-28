package io.jacob.igozogo.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.jacob.igozogo.core.design.component.IgozogoScaffold
import io.jacob.igozogo.core.design.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingRoute(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean
) {
    IgozogoScaffold(
        modifier = modifier,
        title = stringResource(designR.string.core_design_setting)
    ) { paddingValues, nestedScrollConnection ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Text(text = "Setting")
        }
    }
}