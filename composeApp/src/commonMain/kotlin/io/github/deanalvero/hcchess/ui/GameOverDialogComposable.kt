package io.github.deanalvero.hcchess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.deanalvero.hcchess.model.GameMode
import io.github.deanalvero.hcchess.model.GameStatus

@Composable
fun GameOverDialogComposable(viewModel: GameViewModel, onRestart: () -> Unit) {
    if (viewModel.gameStatus == GameStatus.ONGOING) return

    val message = if (viewModel.gameStatus == GameStatus.WHITE_WINS) "White Wins!" else "Black Wins!"
    val mode = "Game Mode: ${viewModel.gameMode.text}"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(enabled = true) {},
        contentAlignment = Alignment.Center
    ) {
        Card(elevation = CardDefaults.cardElevation(8.dp)) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(message, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(mode)
                if (viewModel.gameMode == GameMode.COMPUTER) {
                    Text("Your Side: ${viewModel.playerSide.text}")
                    Text("Difficulty: ${viewModel.difficulty.text}")
                }
                Spacer(Modifier.height(16.dp))
                Button(onClick = onRestart) {
                    Icon(Icons.Default.Refresh, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Play Again")
                }
            }
        }
    }
}
