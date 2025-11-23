package io.github.deanalvero.hcchess.model

data class GameState(
    val board: Board,
    val currentPlayer: Player,
    val moveHistory: List<Move> = emptyList(),
    val status: GameStatus = GameStatus.ONGOING
)
