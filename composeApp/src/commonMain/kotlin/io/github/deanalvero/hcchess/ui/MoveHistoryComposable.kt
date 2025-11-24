package io.github.deanalvero.hcchess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.deanalvero.hcchess.model.Move
import io.github.deanalvero.hcchess.model.PawnDirection
import io.github.deanalvero.hcchess.model.Player
import io.github.deanalvero.hcchess.utils.toDrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MoveHistoryComposable(moves: List<Move>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    LaunchedEffect(moves.size) {
        if (moves.isNotEmpty()) {
            val lastIndex = (moves.size - 1) / 2
            listState.animateScrollToItem(lastIndex)
        }
    }
    Column(modifier.background(Color(0xFFB58863)).fillMaxHeight().padding(16.dp)) {
        Text("Move History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            val pairs = moves.chunked(2)
            itemsIndexed(pairs) { index, pair ->
                val whiteMove = pair[0]
                val blackMove = pair.getOrNull(1)

                Row(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${index + 1}.", fontWeight = FontWeight.Bold)
                    PieceMoveItemComposable(whiteMove, Color.White)
                    if (blackMove != null) {
                        PieceMoveItemComposable(blackMove, Color.Black)
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.PieceMoveItemComposable(move: Move, color: Color) {
    Box(contentAlignment = Alignment.Center) {
        Icon(
            painter = painterResource(move.piece.type.toDrawableResource()),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

        val piece = move.piece
        if (piece.direction != PawnDirection.NONE) {
            val arrowIcon = if (piece.direction == PawnDirection.UP) {
                Icons.Default.ArrowUpward
            } else {
                Icons.Default.ArrowDownward
            }

            val arrowColor = if (piece.player == Player.WHITE) {
                if (piece.direction == PawnDirection.UP) {
                    Color.Green
                } else {
                    Color.Red
                }
            } else {
                if (piece.direction == PawnDirection.UP) {
                    Color.Red
                } else {
                    Color.Green
                }
            }

            Icon(
                imageVector = arrowIcon,
                contentDescription = "Pawn direction: ${piece.direction}",
                tint = arrowColor.copy(alpha = 0.7f),
                modifier = Modifier.wrapContentSize()
            )
        }
    }
    Text(
        "${move.from}-${move.to}",
        modifier = Modifier.weight(1f)
    )
}
