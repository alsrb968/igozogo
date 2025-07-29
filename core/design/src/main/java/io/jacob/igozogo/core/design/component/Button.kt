package io.jacob.igozogo.core.design.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.R
import io.jacob.igozogo.core.design.icon.IgozogoIconButtonSizes
import io.jacob.igozogo.core.design.icon.IgozogoIconSizes
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews

@Composable
fun ToggleFollowIconButton(
    isFollowed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            // TODO: think about animating these icons
            imageVector = when {
                isFollowed -> IgozogoIcons.Check
                else -> IgozogoIcons.Add
            },
            contentDescription = when {
                isFollowed -> stringResource(R.string.core_design_cd_following)
                else -> stringResource(R.string.core_design_cd_not_following)
            },
            tint = animateColorAsState(
                when {
                    isFollowed -> MaterialTheme.colorScheme.onPrimary
                    else -> MaterialTheme.colorScheme.primary
                }
            ).value,
            modifier = Modifier
                .shadow(
                    elevation = animateDpAsState(3.dp).value,
                    shape = CircleShape
                )
                .background(
                    color = animateColorAsState(
                        when {
                            isFollowed -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.surfaceContainerHighest
                        }
                    ).value,
                    shape = CircleShape
                )
                .padding(4.dp)
        )
    }
}

@Composable
fun LargeIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isChecked: Boolean = false,
    icon: ImageVector,
    checkedIcon: ImageVector? = null,
) {
    IconButton(
        modifier = modifier.size(IgozogoIconButtonSizes.Large),
        onClick = onClick
    ) {
        val imageVector = if (isChecked) checkedIcon ?: icon else icon
        Icon(
            modifier = Modifier.size(IgozogoIconSizes.Large),
            imageVector = imageVector,
            contentDescription = null,
        )
    }
}

@Composable
fun MediumIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isChecked: Boolean = false,
    icon: ImageVector,
    checkedIcon: ImageVector? = null,
) {
    IconButton(
        modifier = modifier.size(IgozogoIconButtonSizes.Medium),
        onClick = onClick
    ) {
        val imageVector = if (isChecked) checkedIcon ?: icon else icon
        Icon(
            modifier = Modifier.size(IgozogoIconSizes.Medium),
            imageVector = imageVector,
            contentDescription = null,
        )
    }
}

@Composable
fun SmallIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isChecked: Boolean = false,
    icon: ImageVector,
    checkedIcon: ImageVector? = null,
) {
    IconButton(
        modifier = modifier.size(IgozogoIconButtonSizes.Small),
        onClick = onClick
    ) {
        val imageVector = if (isChecked) checkedIcon ?: icon else icon
        Icon(
            modifier = Modifier.size(IgozogoIconSizes.Small),
            imageVector = imageVector,
            contentDescription = null,
        )
    }
}

@DevicePreviews
@Composable
private fun ToggleFollowIconButtonPreview() {
    IgozogoTheme {
        Column {
            ToggleFollowIconButton(
                isFollowed = true,
                onClick = {}
            )
            ToggleFollowIconButton(
                isFollowed = false,
                onClick = {}
            )
        }
    }
}