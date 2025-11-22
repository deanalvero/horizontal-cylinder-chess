package io.github.deanalvero.hcchess.model

data class Piece(
    val player: Player,
    val type: PieceType,
    val direction: PawnDirection = PawnDirection.NONE
)