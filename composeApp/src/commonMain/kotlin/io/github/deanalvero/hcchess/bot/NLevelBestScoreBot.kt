package io.github.deanalvero.hcchess.bot

import io.github.deanalvero.hcchess.engine.MoveValidator
import io.github.deanalvero.hcchess.model.GameState
import io.github.deanalvero.hcchess.model.GameStatus
import io.github.deanalvero.hcchess.model.Move
import io.github.deanalvero.hcchess.model.PieceType
import io.github.deanalvero.hcchess.model.Player
import kotlinx.coroutines.yield
import kotlin.math.max
import kotlin.math.min

open class NLevelBestScoreBot(val searchDepth: Int) : BotStrategy {
    override suspend fun calculateMove(gameState: GameState): Move? {
        yield()

        val isWhitePlayer = gameState.currentPlayer == Player.WHITE
        val result = bestMove(
            gameState = gameState,
            depth = searchDepth,
            whiteScore = Int.MIN_VALUE,
            blackScore = Int.MAX_VALUE,
            isWhitePlayer = isWhitePlayer
        )
        return result.move
    }

    private suspend fun bestMove(
        gameState: GameState,
        depth: Int,
        whiteScore: Int,
        blackScore: Int,
        isWhitePlayer: Boolean
    ): ScoredMove {
        if (depth == 0) {
            return ScoredMove(null, BoardEvaluator.evaluate(gameState.board))
        }

        if (gameState.status != GameStatus.ONGOING) {
            val score = when (gameState.status) {
                GameStatus.WHITE_WINS -> 69 + depth
                GameStatus.BLACK_WINS -> -69 - depth
                else -> 0
            }
            return ScoredMove(null, score)
        }

        var currentWhiteScore = whiteScore
        var currentBlackScore = blackScore

        val validMoves = getAllValidMoves(gameState)

        if (validMoves.isEmpty()) {
            return ScoredMove(null, 0)
        }

        var bestMove: Move? = validMoves.first()

        if (isWhitePlayer) {
            var maxWhiteScore = Int.MIN_VALUE
            for (move in validMoves) {
                if (depth == searchDepth) yield()

                val nextBoard = gameState.board.applyMove(move)

                if (move.capturedPiece?.type == PieceType.KING) {
                    return ScoredMove(move, 69 + depth)
                }

                val nextState = gameState.copy(
                    board = nextBoard,
                    currentPlayer = Player.BLACK
                )

                val score = bestMove(
                    gameState = nextState,
                    depth = depth - 1,
                    whiteScore = currentWhiteScore,
                    blackScore = currentBlackScore,
                    isWhitePlayer = false
                ).score

                if (score > maxWhiteScore) {
                    maxWhiteScore = score
                    bestMove = move
                }
                currentWhiteScore = max(currentWhiteScore, score)
                if (currentBlackScore <= currentWhiteScore) break
            }
            return ScoredMove(bestMove, maxWhiteScore)
        } else {
            var maxBlackScore = Int.MAX_VALUE
            for (move in validMoves) {
                if (depth == searchDepth) yield()

                val nextBoard = gameState.board.applyMove(move)

                if (move.capturedPiece?.type == PieceType.KING) {
                    return ScoredMove(move, -69 - depth)
                }

                val nextState = gameState.copy(
                    board = nextBoard,
                    currentPlayer = Player.WHITE
                )

                val score = bestMove(
                    gameState = nextState,
                    depth = depth - 1,
                    whiteScore = currentWhiteScore,
                    blackScore = currentBlackScore,
                    isWhitePlayer = true
                ).score

                if (score < maxBlackScore) {
                    maxBlackScore = score
                    bestMove = move
                }
                currentBlackScore = min(currentBlackScore, score)
                if (currentBlackScore <= currentWhiteScore) break
            }
            return ScoredMove(bestMove, maxBlackScore)
        }
    }

    private fun getAllValidMoves(gameState: GameState): List<Move> {
        val moves = mutableListOf<Move>()
        gameState.board.pieces.forEach { (pos, piece) ->
            if (piece.player == gameState.currentPlayer) {
                moves.addAll(MoveValidator.getValidMovesForPiece(piece, pos, gameState))
            }
        }
        val (captures, quietMoves) = moves.partition { it.capturedPiece != null }
        return captures.shuffled() + quietMoves.shuffled()
    }

    private data class ScoredMove(val move: Move?, val score: Int)
}