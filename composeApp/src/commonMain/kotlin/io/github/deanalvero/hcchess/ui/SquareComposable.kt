package io.github.deanalvero.hcchess.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.deanalvero.hcchess.model.Piece
import io.github.deanalvero.hcchess.model.Position

@Composable
fun SquareComposable(
    position: Position,
    piece: Piece?,
    isSelected: Boolean,
    isPossibleMove: Boolean,
    isCheck: Boolean,
    isAttacker: Boolean,
    onClick: (Position) -> Unit,
    modifier: Modifier = Modifier
) {
    val isLight = (position.file + position.rank) % 2 != 0
    val baseColor = if (isLight) Color(0xFFF0D9B5) else Color(0xFFB58863)
    val textColor = if (isLight) Color(0xFFB58863) else Color(0xFFF0D9B5)

    val backgroundColor = when {
        isCheck -> Color(0xFFE53935)
        isAttacker -> Color(0xFFEF9A9A)
        isSelected -> Color.Yellow.copy(alpha = 0.5f)
        else -> baseColor
    }

    Box(
        modifier = modifier
            .background(backgroundColor)
            .border(0.5.dp, Color.Black.copy(alpha = 0.1f))
            .clickable { onClick(position) },
        contentAlignment = Alignment.Center
    ) {
        if (isPossibleMove) {
            Box(Modifier.size(16.dp).background(Color.Green.copy(alpha = 0.5f)))
        }

        if (piece != null) {
            PieceComposable(piece)
        }

        Text(
            text = "$position",
            fontSize = 8.sp,
            color = textColor,
            modifier = Modifier.align(Alignment.TopStart).padding(1.dp)
        )
    }
}
