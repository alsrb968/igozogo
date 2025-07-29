@file:OptIn(ExperimentalMaterial3Api::class)

package io.jacob.igozogo.core.design.provider

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.staticCompositionLocalOf

val LocalTopAppBarScrollBehavior = staticCompositionLocalOf<TopAppBarScrollBehavior> {
    error("TopAppBarScrollBehavior not provided")
}