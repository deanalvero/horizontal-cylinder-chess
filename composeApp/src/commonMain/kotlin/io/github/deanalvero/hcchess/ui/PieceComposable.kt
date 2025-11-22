package io.github.deanalvero.hcchess.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.deanalvero.hcchess.model.PawnDirection
import io.github.deanalvero.hcchess.model.Piece
import io.github.deanalvero.hcchess.model.PieceType
import io.github.deanalvero.hcchess.model.Player

@Composable
fun PieceView(piece: Piece) {
    val symbol = when (piece.type) {
        PieceType.KING -> if (piece.player == Player.WHITE) "♔" else "♚"
        PieceType.QUEEN -> if (piece.player == Player.WHITE) "♕" else "♛"
        PieceType.ROOK -> if (piece.player == Player.WHITE) "♖" else "♜"
        PieceType.BISHOP -> if (piece.player == Player.WHITE) "♗" else "♝"
        PieceType.KNIGHT -> if (piece.player == Player.WHITE) "♘" else "♞"
        PieceType.CENTRAL_PAWN, PieceType.WRAP_PAWN ->
            if (piece.player == Player.WHITE) "♙" else "♟"
    }

    val textColor = if (piece.player == Player.WHITE) Color.White else Color.Black

    Box(contentAlignment = Alignment.Center) {
        Text(
            text = symbol,
            fontSize = 32.sp,
            color = textColor,
            fontWeight = FontWeight.Bold
        )

        if (piece.direction != PawnDirection.NONE) {
            val icon = if (piece.direction == PawnDirection.UP)
                Icons.Default.ArrowDownward else Icons.Default.ArrowUpward

            val arrowColor = if (piece.player == Player.WHITE) Color.Green else Color.Red

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = arrowColor.copy(alpha = 0.7f),
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
