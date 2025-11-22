package io.github.deanalvero.hcchess

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.deanalvero.hcchess.ui.BoardComposable
import io.github.deanalvero.hcchess.ui.GameViewModel

@Composable
fun App() {
    val viewModel = remember { GameViewModel() }
    MaterialTheme {
        BoardComposable(viewModel)
    }
}
