package io.github.deanalvero.hcchess.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.deanalvero.hcchess.model.Board
import io.github.deanalvero.hcchess.model.GameState
import io.github.deanalvero.hcchess.model.GameStatus
import io.github.deanalvero.hcchess.model.Move
import io.github.deanalvero.hcchess.model.PieceType
import io.github.deanalvero.hcchess.model.Player
import io.github.deanalvero.hcchess.model.Position

class GameEngine {
    var gameState by mutableStateOf(GameState(Board.createDefault(), Player.WHITE))
        private set

    private var currentValidMoves: Set<Move> = emptySet()

    init {
        calculateMoves()
    }

    private fun calculateMoves() {
        val moves = mutableSetOf<Move>()
        gameState.board.pieces.forEach { (pos, piece) ->
            if (piece.player == gameState.currentPlayer) {
                moves.addAll(MoveValidator.getValidMovesForPiece(piece, pos, gameState))
            }
        }
        currentValidMoves = moves
    }

    fun getValidMovesAt(position: Position): Set<Position> {
        return currentValidMoves.filter { it.from == position }.map { it.to }.toSet()
    }

    fun onMove(from: Position, to: Position): Boolean {
        if (gameState.status != GameStatus.ONGOING) return false

        val move = currentValidMoves.find {
            it.from == from && it.to == to && (it.promotionType == null || it.promotionType == PieceType.QUEEN)
        } ?: return false

        val newBoard = gameState.board.applyMove(move)

        var newStatus = GameStatus.ONGOING
        if (move.capturedPiece?.type == PieceType.KING) {
            newStatus = if (gameState.currentPlayer == Player.WHITE) {
                GameStatus.WHITE_WINS
            } else {
                GameStatus.BLACK_WINS
            }
        }

        gameState = gameState.copy(
            board = newBoard,
            currentPlayer = gameState.currentPlayer.opponent,
            moveHistory = gameState.moveHistory + move,
            status = newStatus
        )

        if (newStatus == GameStatus.ONGOING) {
            calculateMoves()
        } else {
            currentValidMoves = emptySet()
        }

        return true
    }

    fun reset() {
        gameState = GameState(Board.createDefault(), Player.WHITE)
        calculateMoves()
    }
}
