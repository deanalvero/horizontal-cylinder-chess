package io.github.deanalvero.hcchess.model

import kotlin.to

data class Board(
    val pieces: Map<Position, Piece>
) {
    fun getPieceAt(position: Position): Piece? = pieces[position]

    fun applyMove(move: Move): Board {
        val newPieces = pieces.toMutableMap()

        newPieces.remove(move.from)

        if (move.isEnPassant) {
            val capturedPawnPos = Position(move.to.file, move.from.rank)
            newPieces.remove(capturedPawnPos)
        }

        val newPiece = if (move.promotionType != null) {
            Piece(move.player, move.promotionType, PawnDirection.NONE)
        } else {
            move.piece
        }
        newPieces[move.to] = newPiece

        if (move.isCastling) {
            val rank = move.from.rank
            if (move.to.file == 6) {
                val rookFrom = Position(7, rank)
                val rookTo = Position(5, rank)
                val rook = pieces[rookFrom]
                if (rook != null) {
                    newPieces.remove(rookFrom)
                    newPieces[rookTo] = rook
                }
            } else if (move.to.file == 2) {
                val rookFrom = Position(0, rank)
                val rookTo = Position(3, rank)
                val rook = pieces[rookFrom]
                if (rook != null) {
                    newPieces.remove(rookFrom)
                    newPieces[rookTo] = rook
                }
            }
        }

        return this.copy(pieces = newPieces)
    }

    companion object {
        fun createDefault(): Board {
            val startingPieces = mutableMapOf<Position, Piece>()
            val majorPieces = listOf(
                PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
                PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
            )

            for (file in 0..7) {
                startingPieces[Position(file, 13)] = Piece(Player.WHITE, PieceType.WRAP_PAWN, PawnDirection.DOWN)
                startingPieces[Position(file, 0)] = Piece(Player.WHITE, majorPieces[file])
                startingPieces[Position(file, 1)] = Piece(Player.WHITE, PieceType.CENTRAL_PAWN, PawnDirection.UP)

                startingPieces[Position(file, 6)] = Piece(Player.BLACK, PieceType.CENTRAL_PAWN, PawnDirection.DOWN)
                startingPieces[Position(file, 7)] = Piece(Player.BLACK, majorPieces[file])
                startingPieces[Position(file, 8)] = Piece(Player.BLACK, PieceType.WRAP_PAWN, PawnDirection.UP)
            }
            return Board(startingPieces)
        }
    }
}