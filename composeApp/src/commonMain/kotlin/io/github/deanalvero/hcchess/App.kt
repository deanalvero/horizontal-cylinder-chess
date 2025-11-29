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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import io.github.deanalvero.hcchess.ui.AboutComposable
import io.github.deanalvero.hcchess.ui.BoardComposable
import io.github.deanalvero.hcchess.ui.GameOverDialogComposable
import io.github.deanalvero.hcchess.ui.GameSetupDialogComposable
import io.github.deanalvero.hcchess.ui.GameViewModel
import io.github.deanalvero.hcchess.ui.MoveHistoryComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel = remember { GameViewModel() }

    var isDropdownMenuShown by remember { mutableStateOf(false) }
    var isSourceDialogShown by remember { mutableStateOf(false) }
    var isSetupDialogShown by remember { mutableStateOf(true) }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Horizontal Cylinder Chess") },
                    actions = {
                        IconButton(onClick = { isDropdownMenuShown = true }) {
                            Icon(Icons.Default.MoreVert, "More")
                        }
                        DropdownMenu(
                            expanded = isDropdownMenuShown,
                            onDismissRequest = { isDropdownMenuShown = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("New Game") },
                                onClick = {
                                    isDropdownMenuShown = false
                                    isSetupDialogShown = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("About") },
                                onClick = {
                                    isDropdownMenuShown = false
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
                                text = "Turn: ${viewModel.currentPlayer.text}",
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
                            Text("Turn: ${viewModel.currentPlayer.text}")
                        }
                    }
                }
                GameOverDialogComposable(
                    viewModel = viewModel,
                    onRestart = {
                        viewModel.reset()
                    },
                )
            }
        }

        if (isSetupDialogShown) {
            GameSetupDialogComposable(
                onDismiss = { isSetupDialogShown = false },
                onStartGame = { gameMode, player, difficulty ->
                    viewModel.startNewGame(gameMode, player, difficulty)
                    isSetupDialogShown = false
                }
            )
        }

        if (isSourceDialogShown) {
            AboutComposable(
                onDismiss = {
                    isSourceDialogShown = false
                },
                onOk = {
                    isSourceDialogShown = false
                }
            )
        }
    }
}
