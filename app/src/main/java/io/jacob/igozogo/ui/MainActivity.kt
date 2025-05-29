package io.jacob.igozogo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import io.jacob.igozogo.ui.theme.IgozogoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IgozogoTheme {
                IgozogoApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IgozogoAppPreview() {
    IgozogoTheme {
        IgozogoApp()
    }
}