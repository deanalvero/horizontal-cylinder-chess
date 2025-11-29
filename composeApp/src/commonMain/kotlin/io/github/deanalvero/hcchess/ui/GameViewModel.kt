package io.github.deanalvero.hcchess.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.deanalvero.hcchess.bot.BotStrategy
import io.github.deanalvero.hcchess.bot.EasyBot
import io.github.deanalvero.hcchess.bot.HardBot
import io.github.deanalvero.hcchess.bot.MediumBot
import io.github.deanalvero.hcchess.engine.GameEngine
import io.github.deanalvero.hcchess.model.Board
import io.github.deanalvero.hcchess.model.Difficulty
import io.github.deanalvero.hcchess.model.GameMode
import io.github.deanalvero.hcchess.model.GameStatus
import io.github.deanalvero.hcchess.model.Move
import io.github.deanalvero.hcchess.model.Player
import io.github.deanalvero.hcchess.model.Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    var gameMode by mutableStateOf(GameMode.SELF)
        private set
    var playerSide by mutableStateOf<Player>(Player.WHITE)
        private set
    var difficulty by mutableStateOf(Difficulty.EASY)
        private set

    private var botStrategy: BotStrategy = EasyBot()
    private var isBotThinking by mutableStateOf(false)

    fun startNewGame(mode: GameMode, side: Player = Player.WHITE, diff: Difficulty = Difficulty.EASY) {
        gameMode = mode
        playerSide = side
        difficulty = diff

        botStrategy = when (diff) {
            Difficulty.EASY -> EasyBot()
            Difficulty.MEDIUM -> MediumBot()
            Difficulty.HARD -> HardBot()
        }
        reset()
    }

    fun onSquareClicked(position: Position) {
        if (isBotThinking) return
        if (gameStatus != GameStatus.ONGOING) return
        if (gameMode == GameMode.COMPUTER && engine.gameState.currentPlayer != playerSide) return

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

                    if (gameMode == GameMode.COMPUTER &&
                        gameStatus == GameStatus.ONGOING &&
                        engine.gameState.currentPlayer != playerSide
                    ) {
                        triggerBotMove()
                    }
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

    private fun triggerBotMove() {
        GlobalScope.launch {
            isBotThinking = true
            val move = botStrategy.calculateMove(engine.gameState)

            withContext(Dispatchers.Main) {
                if (move != null) {
                    engine.onMove(move.from, move.to)
                }
                isBotThinking = false
            }
        }
    }

    fun reset() {
        engine.reset()
        selectedPosition = null
        validMoves = emptySet()
        isBotThinking = false

        if (gameMode == GameMode.COMPUTER && playerSide == Player.BLACK) {
            triggerBotMove()
        }
    }
}
