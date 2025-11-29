package io.github.deanalvero.hcchess.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.deanalvero.hcchess.model.PawnDirection
import io.github.deanalvero.hcchess.model.Piece
import io.github.deanalvero.hcchess.model.Player
import io.github.deanalvero.hcchess.utils.toDrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun PieceComposable(
    piece: Piece,
    modifier: Modifier = Modifier
) {
    val pieceTint = if (piece.player == Player.WHITE) Color.White else Color.Black

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(piece.type.toDrawableResource()),
            contentDescription = "${piece.player.text} ${piece.type.name}",
            tint = pieceTint,
            modifier = Modifier.fillMaxSize().padding(4.dp)
        )

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
}
