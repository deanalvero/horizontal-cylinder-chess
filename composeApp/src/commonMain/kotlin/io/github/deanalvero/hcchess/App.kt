package io.github.deanalvero.hcchess

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.deanalvero.hcchess.ui.BoardComposable
import io.github.deanalvero.hcchess.ui.GameOverDialogComposable
import io.github.deanalvero.hcchess.ui.GameViewModel
import io.github.deanalvero.hcchess.ui.MoveHistoryComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel = remember { GameViewModel() }

    var isMenuShown by remember { mutableStateOf(false) }
    var isSourceDialogShown by remember { mutableStateOf(false) }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Horizontal Cylinder Chess") },
                    actions = {
                        IconButton(onClick = { viewModel.reset() }) {
                            Icon(Icons.Default.Refresh, "Restart")
                        }

                        IconButton(onClick = { isMenuShown = true }) {
                            Icon(Icons.Default.MoreVert, "More")
                        }
                        DropdownMenu(
                            expanded = isMenuShown,
                            onDismissRequest = { isMenuShown = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("About") },
                                onClick = {
                                    isMenuShown = false
                                    isSourceDialogShown = true
                                }
                            )
                        }
                    }
                )
            }
        ) { padding ->
            BoxWithConstraints(
                Modifier.padding(padding).fillMaxSize()
            ) {
                if (maxWidth > maxHeight) {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(Modifier.weight(1f))
                        Column(Modifier.weight(1.5f)) {
                            MoveHistoryComposable(
                                moves = viewModel.moveHistory,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Turn: ${viewModel.currentPlayer.id}",
                                modifier = Modifier.fillMaxWidth().background(Color(0xFFF0D9B5)),
                                textAlign = TextAlign.Center
                            )
                        }
                        BoardComposable(
                            viewModel = viewModel,
                            modifier = Modifier
                                .width(this@BoxWithConstraints.maxHeight - 48.dp)
                                .fillMaxHeight()
                        )
                        Spacer(Modifier.weight(1f))
                    }
                } else {
                    Column(Modifier.fillMaxSize()) {
                        BoardComposable(
                            viewModel = viewModel,
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                        Row(
                            Modifier.fillMaxWidth().background(Color(0xFFF0D9B5)).padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Turn: ${viewModel.currentPlayer.id}")
                        }
                    }
                }
                GameOverDialogComposable(
                    status = viewModel.gameStatus,
                    onRestart = {
                        viewModel.reset()
                    }
                )
            }
        }

        if (isSourceDialogShown) {
            AlertDialog(
                onDismissRequest = { isSourceDialogShown = false },
                confirmButton = {
                    TextButton(onClick = { isSourceDialogShown = false }) {
                        Text("OK")
                    }
                },
                title = { Text("About") },
                text = {
                    SelectionContainer {
                        Text("Horizontal Cylinder Chess source code can be found at https://github.com/deanalvero/horizontal-cylinder-chess")
                    }
                }
            )
        }
    }
}
