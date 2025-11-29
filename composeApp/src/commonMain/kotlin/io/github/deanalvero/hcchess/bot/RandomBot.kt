package io.github.deanalvero.hcchess.bot

import io.github.deanalvero.hcchess.engine.MoveValidator
import io.github.deanalvero.hcchess.model.GameState
import io.github.deanalvero.hcchess.model.Move
import kotlinx.coroutines.delay

open class RandomBot : BotStrategy {
    override suspend fun calculateMove(gameState: GameState): Move? {
        delay(500)
        val allMoves = mutableListOf<Move>()
        gameState.board.pieces.forEach { (pos, piece) ->
            if (piece.player == gameState.currentPlayer) {
                allMoves.addAll(MoveValidator.getValidMovesForPiece(piece, pos, gameState))
            }
        }
        return allMoves.randomOrNull()
    }
}
