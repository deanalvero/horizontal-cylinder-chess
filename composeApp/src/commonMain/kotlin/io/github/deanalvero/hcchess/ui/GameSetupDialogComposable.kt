package io.github.deanalvero.hcchess.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import io.github.deanalvero.hcchess.model.Difficulty
import io.github.deanalvero.hcchess.model.GameMode
import io.github.deanalvero.hcchess.model.Player

@Composable
fun GameSetupDialogComposable(
    onDismiss: () -> Unit,
    onStartGame: (GameMode, Player, Difficulty) -> Unit
) {
    var selectedMode by remember { mutableStateOf(GameMode.COMPUTER) }
    var selectedSide by remember { mutableStateOf<Player>(Player.WHITE) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.MEDIUM) }

    val scrollState = rememberScrollState()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Game Setup") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Text("Game Mode", fontWeight = FontWeight.Bold)
                Row {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedMode == GameMode.COMPUTER,
                            onClick = { selectedMode = GameMode.COMPUTER }
                        )
                        Text("Computer", Modifier.clickable { selectedMode = GameMode.COMPUTER })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedMode == GameMode.SELF,
                            onClick = { selectedMode = GameMode.SELF }
                        )
                        Text("Self", Modifier.clickable { selectedMode = GameMode.SELF })
                    }
                }

                if (selectedMode == GameMode.COMPUTER) {
                    Text("Your Side", fontWeight = FontWeight.Bold)
                    Row {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedSide == Player.WHITE,
                                onClick = { selectedSide = Player.WHITE }
                            )
                            Text("White", Modifier.clickable { selectedSide = Player.WHITE })
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedSide == Player.BLACK,
                                onClick = { selectedSide = Player.BLACK }
                            )
                            Text("Black", Modifier.clickable { selectedSide = Player.BLACK })
                        }
                    }
                    Text("Difficulty", fontWeight = FontWeight.Bold)
                    Row {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedDifficulty == Difficulty.EASY,
                                onClick = { selectedDifficulty = Difficulty.EASY }
                            )
                            Text("Easy", Modifier.clickable { selectedDifficulty = Difficulty.EASY })
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedDifficulty == Difficulty.MEDIUM,
                                onClick = { selectedDifficulty = Difficulty.MEDIUM }
                            )
                            Text("Medium", Modifier.clickable { selectedDifficulty = Difficulty.MEDIUM })
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedDifficulty == Difficulty.HARD,
                                onClick = { selectedDifficulty = Difficulty.HARD }
                            )
                            Text("Hard", Modifier.clickable { selectedDifficulty = Difficulty.HARD })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onStartGame(selectedMode, selectedSide, selectedDifficulty) }) {
                Text("Start")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
