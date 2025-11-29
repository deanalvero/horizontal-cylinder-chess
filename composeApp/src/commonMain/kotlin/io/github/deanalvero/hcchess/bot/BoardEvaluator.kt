package io.github.deanalvero.hcchess.bot

import io.github.deanalvero.hcchess.model.Board
import io.github.deanalvero.hcchess.model.PieceType
import io.github.deanalvero.hcchess.model.Player

object BoardEvaluator {
    fun evaluate(board: Board): Int {
        var score = 0
        board.pieces.values.forEach { piece ->
            val value = getPieceValue(piece.type)
            score += if (piece.player == Player.WHITE) value else -value
        }
        return score
    }

    private fun getPieceValue(type: PieceType): Int {
        return when (type) {
            PieceType.KING -> 69
            PieceType.QUEEN -> 9
            PieceType.ROOK -> 5
            PieceType.BISHOP -> 4
            PieceType.KNIGHT -> 3
            PieceType.CENTRAL_PAWN, PieceType.WRAP_PAWN -> 1
        }
    }
}
