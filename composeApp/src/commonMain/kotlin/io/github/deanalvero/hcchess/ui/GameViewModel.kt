package io.github.deanalvero.hcchess.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.deanalvero.hcchess.engine.GameEngine
import io.github.deanalvero.hcchess.model.Board
import io.github.deanalvero.hcchess.model.GameStatus
import io.github.deanalvero.hcchess.model.Move
import io.github.deanalvero.hcchess.model.Player
import io.github.deanalvero.hcchess.model.Position

class GameViewModel {
    private val engine = GameEngine()

    val board: Board get() = engine.gameState.board
    val currentPlayer: Player get() = engine.gameState.currentPlayer

    var selectedPosition by mutableStateOf<Position?>(null)
        private set
    var validMoves by mutableStateOf<Set<Position>>(emptySet())
        private set

    val gameStatus: GameStatus get() = engine.gameState.status
    val moveHistory: List<Move> get() = engine.gameState.moveHistory

    fun onSquareClicked(position: Position) {
        val selected = selectedPosition

        if (selected == null) {
            val piece = board.getPieceAt(position)
            if (piece != null && piece.player == currentPlayer) {
                selectedPosition = position
                validMoves = engine.getValidMovesAt(position)
            }
        } else {
            if (position in validMoves) {
                val success = engine.onMove(selected, position)
                if (success) {
                    selectedPosition = null
                    validMoves = emptySet()
                } else {
                    selectedPosition = null
                    validMoves = emptySet()
                }
            } else {
                val piece = board.getPieceAt(position)
                if (piece != null && piece.player == currentPlayer) {
                    selectedPosition = position
                    validMoves = engine.getValidMovesAt(position)
                } else {
                    selectedPosition = null
                    validMoves = emptySet()
                }
            }
        }
    }

    fun reset() {
        engine.reset()
        selectedPosition = null
        validMoves = emptySet()
    }
}
