package io.github.deanalvero.hcchess.utils

import hcchess.composeapp.generated.resources.Res
import hcchess.composeapp.generated.resources.chess_bishop
import hcchess.composeapp.generated.resources.chess_king
import hcchess.composeapp.generated.resources.chess_knight
import hcchess.composeapp.generated.resources.chess_pawn
import hcchess.composeapp.generated.resources.chess_queen
import hcchess.composeapp.generated.resources.chess_rook
import io.github.deanalvero.hcchess.model.PieceType
import org.jetbrains.compose.resources.DrawableResource

fun floorMod(a: Int, b: Int): Int {
    return ((a % b) + b) % b
}

fun PieceType.toDrawableResource(): DrawableResource {
    return when (this) {
        PieceType.KING -> Res.drawable.chess_king
        PieceType.QUEEN -> Res.drawable.chess_queen
        PieceType.ROOK -> Res.drawable.chess_rook
        PieceType.BISHOP -> Res.drawable.chess_bishop
        PieceType.KNIGHT -> Res.drawable.chess_knight
        PieceType.CENTRAL_PAWN, PieceType.WRAP_PAWN -> Res.drawable.chess_pawn
    }
}
