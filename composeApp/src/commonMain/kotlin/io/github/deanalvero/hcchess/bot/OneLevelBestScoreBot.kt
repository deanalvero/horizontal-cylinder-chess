package io.github.deanalvero.hcchess.bot

import io.github.deanalvero.hcchess.engine.MoveValidator
import io.github.deanalvero.hcchess.model.GameState
import io.github.deanalvero.hcchess.model.Move
import io.github.deanalvero.hcchess.model.Player
import kotlinx.coroutines.delay

open class OneLevelBestScoreBot : BotStrategy {
    override suspend fun calculateMove(gameState: GameState): Move? {
        delay(500)
        val allMoves = mutableListOf<Move>()
        gameState.board.pieces.forEach { (pos, piece) ->
            if (piece.player == gameState.currentPlayer) {
                allMoves.addAll(MoveValidator.getValidMovesForPiece(piece, pos, gameState))
            }
        }
        val validMoves = allMoves.shuffled()
        if (validMoves.isEmpty()) return null

        var bestMove: Move? = null
        var bestScore = if (gameState.currentPlayer == Player.WHITE) Int.MIN_VALUE else Int.MAX_VALUE

        for (move in validMoves) {
            val nextBoard = gameState.board.applyMove(move)
            val score = BoardEvaluator.evaluate(nextBoard)
            if (gameState.currentPlayer == Player.WHITE) {
                if (score > bestScore) {
                    bestScore = score
                    bestMove = move
                }
            } else {
                if (score < bestScore) {
                    bestScore = score
                    bestMove = move
                }
            }
        }
        return bestMove ?: validMoves.random()
    }
}
