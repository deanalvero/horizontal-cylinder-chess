package io.github.deanalvero.hcchess.model

data class Move(
    val player: Player,
    val from: Position,
    val to: Position,
    val piece: Piece,
    val capturedPiece: Piece? = null,
    val isEnPassant: Boolean = false,
    val promotionType: PieceType? = null,
    val isCastling: Boolean = false
)
