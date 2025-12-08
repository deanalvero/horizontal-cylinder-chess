package io.github.deanalvero.hcchess.model

data class GameState(
    val board: Board,
    val currentPlayer: Player,
    val moveHistory: List<Move> = emptyList(),
    val status: GameStatus = GameStatus.ONGOING,
    val hasKingMoved: Map<Player, Boolean> = mapOf(Player.WHITE to false, Player.BLACK to false),
    val hasRookMoved: Map<Position, Boolean> = emptyMap()
)
