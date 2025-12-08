package io.github.deanalvero.hcchess.engine

import io.github.deanalvero.hcchess.model.Board
import io.github.deanalvero.hcchess.model.GameState
import io.github.deanalvero.hcchess.model.Move
import io.github.deanalvero.hcchess.model.Piece
import io.github.deanalvero.hcchess.model.PieceType
import io.github.deanalvero.hcchess.model.Player
import io.github.deanalvero.hcchess.model.Position
import kotlin.math.abs

object MoveValidator {
    fun getValidMovesForPiece(piece: Piece, at: Position, gameState: GameState): Set<Move> {
        return when (piece.type) {
            PieceType.KING -> getKingMoves(piece, at, gameState)
            PieceType.QUEEN -> getQueenMoves(piece, at, gameState.board)
            PieceType.ROOK -> getRookMoves(piece, at, gameState.board)
            PieceType.BISHOP -> getBishopMoves(piece, at, gameState.board)
            PieceType.KNIGHT -> getKnightMoves(piece, at, gameState.board)
            PieceType.CENTRAL_PAWN -> getCentralPawnMoves(piece, at, gameState)
            PieceType.WRAP_PAWN -> getWrapPawnMoves(piece, at, gameState)
        }
    }

    private fun getSlidingMoves(
        piece: Piece, at: Position, board: Board, directions: List<Pair<Int, Int>>
    ): Set<Move> {
        val moves = mutableSetOf<Move>()
        for ((fileOffset, rankOffset) in directions) {
            var currentPos = at.plus(fileOffset, rankOffset)
            while (currentPos != null) {
                val pieceAtTarget = board.getPieceAt(currentPos)
                if (pieceAtTarget == null) {
                    moves.add(Move(piece.player, at, currentPos, piece))
                    currentPos = currentPos.plus(fileOffset, rankOffset)
                } else {
                    if (pieceAtTarget.player != piece.player) {
                        moves.add(Move(piece.player, at, currentPos, piece, capturedPiece = pieceAtTarget))
                    }
                    break
                }
            }
        }
        return moves
    }

    private fun getSingleMoves(
        piece: Piece, at: Position, board: Board, offsets: List<Pair<Int, Int>>
    ): Set<Move> {
        val moves = mutableSetOf<Move>()
        for ((fileOffset, rankOffset) in offsets) {
            val targetPos = at.plus(fileOffset, rankOffset)
            if (targetPos != null) {
                val pieceAtTarget = board.getPieceAt(targetPos)
                if (pieceAtTarget == null) {
                    moves.add(Move(piece.player, at, targetPos, piece))
                } else if (pieceAtTarget.player != piece.player) {
                    moves.add(Move(piece.player, at, targetPos, piece, capturedPiece = pieceAtTarget))
                }
            }
        }
        return moves
    }

    private fun getKingMoves(piece: Piece, at: Position, gameState: GameState): Set<Move> {
        val offsets = listOf(
            -1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1
        )
        val normalMoves = getSingleMoves(piece, at, gameState.board, offsets)
        val castlingMoves = getCastlingMoves(piece, at, gameState)
        return normalMoves + castlingMoves
    }

    private fun getCastlingMoves(piece: Piece, at: Position, gameState: GameState): Set<Move> {
        val moves = mutableSetOf<Move>()

        if (gameState.hasKingMoved[piece.player] == true) return emptySet()
        if (isSquareUnderAttack(at, piece.player, gameState)) return emptySet()

        val rank = at.rank

        val shortRookPos = Position(7, rank)
        if (canCastle(at, shortRookPos, listOf(5, 6), gameState, piece.player)) {
            val targetPos = Position(6, rank)
            moves.add(Move(piece.player, at, targetPos, piece, isCastling = true))
        }

        val longRookPos = Position(0, rank)
        if (canCastle(at, longRookPos, listOf(1, 2, 3), gameState, piece.player)) {
            val targetPos = Position(2, rank)
            moves.add(Move(piece.player, at, targetPos, piece, isCastling = true))
        }
        return moves
    }

    private fun canCastle(
        kingPos: Position,
        rookPos: Position,
        filesToCheck: List<Int>,
        gameState: GameState,
        player: Player
    ): Boolean {
        val rook = gameState.board.getPieceAt(rookPos)
        if (rook == null || rook.type != PieceType.ROOK || rook.player != player) return false
        if (gameState.hasRookMoved[rookPos] == true) return false

        for (file in filesToCheck) {
            if (gameState.board.getPieceAt(Position(file, kingPos.rank)) != null) return false
        }

        val direction = if (rookPos.file > kingPos.file) 1 else -1
        val crossSquare = Position(kingPos.file + direction, kingPos.rank)
        val landSquare = Position(kingPos.file + (direction * 2), kingPos.rank)

        if (isSquareUnderAttack(crossSquare, player, gameState)) return false
        if (isSquareUnderAttack(landSquare, player, gameState)) return false
        return true
    }

    private fun isSquareUnderAttack(target: Position, defender: Player, gameState: GameState): Boolean {
        gameState.board.pieces.forEach { (pos, piece) ->
            if (piece.player != defender) {
                val moves = if (piece.type == PieceType.KING) {
                    val offsets = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
                    getSingleMoves(piece, pos, gameState.board, offsets)
                } else {
                    getValidMovesForPiece(piece, pos, gameState)
                }
                if (moves.any { it.to == target }) return true
            }
        }
        return false
    }

    private fun getQueenMoves(piece: Piece, at: Position, board: Board) =
        getRookMoves(piece, at, board) + getBishopMoves(piece, at, board)

    private fun getRookMoves(piece: Piece, at: Position, board: Board): Set<Move> {
        val directions = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
        return getSlidingMoves(piece, at, board, directions)
    }

    private fun getBishopMoves(piece: Piece, at: Position, board: Board): Set<Move> {
        val directions = listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1)
        return getSlidingMoves(piece, at, board, directions)
    }

    private fun getKnightMoves(piece: Piece, at: Position, board: Board): Set<Move> {
        val offsets = listOf(
            -1 to 2, 1 to 2, -1 to -2, 1 to -2, -2 to 1, -2 to -1, 2 to 1, 2 to -1
        )
        return getSingleMoves(piece, at, board, offsets)
    }

    private fun getPawnMoves(
        piece: Piece, at: Position, gameState: GameState,
        rankDir: Int, startRank: Int, promotionRank: Int
    ): Set<Move> {
        val moves = mutableSetOf<Move>()
        val board = gameState.board

        val oneStep = at.plus(0, rankDir)
        if (oneStep != null && board.getPieceAt(oneStep) == null) {
            if (oneStep.rank == promotionRank) {
                moves.addAll(createPromotionMoves(piece, at, oneStep))
            } else {
                moves.add(Move(piece.player, at, oneStep, piece))
            }

            if (at.rank == startRank) {
                val twoSteps = at.plus(0, rankDir * 2)
                if (twoSteps != null && board.getPieceAt(twoSteps) == null) {
                    moves.add(Move(piece.player, at, twoSteps, piece))
                }
            }
        }

        listOf(-1, 1).forEach { fileOffset ->
            val capturePos = at.plus(fileOffset, rankDir)
            if (capturePos != null) {
                val target = board.getPieceAt(capturePos)
                if (target != null && target.player != piece.player) {
                    if (capturePos.rank == promotionRank) {
                        moves.addAll(createPromotionMoves(piece, at, capturePos, target))
                    } else {
                        moves.add(Move(piece.player, at, capturePos, piece, capturedPiece = target))
                    }
                }
            }
        }

        val lastMove = gameState.moveHistory.lastOrNull()
        if (lastMove != null &&
            (lastMove.piece.type == PieceType.CENTRAL_PAWN || lastMove.piece.type == PieceType.WRAP_PAWN) &&
            abs(lastMove.from.rank - lastMove.to.rank) == 2 &&
            lastMove.to.rank == at.rank &&
            abs(lastMove.to.file - at.file) == 1
        ) {
            val epTarget = at.plus(lastMove.to.file - at.file, rankDir)
            if (epTarget != null) {
                moves.add(Move(piece.player, at, epTarget, piece, capturedPiece = lastMove.piece, isEnPassant = true))
            }
        }

        return moves
    }

    private fun getCentralPawnMoves(piece: Piece, at: Position, gs: GameState): Set<Move> {
        return if (piece.player == Player.WHITE) {
            getPawnMoves(piece, at, gs, 1, 1, 7)
        } else {
            getPawnMoves(piece, at, gs, -1, 6, 0)
        }
    }

    private fun getWrapPawnMoves(piece: Piece, at: Position, gs: GameState): Set<Move> {
        return if (piece.player == Player.WHITE) {
            getPawnMoves(piece, at, gs, -1, 13, 7)
        } else {
            getPawnMoves(piece, at, gs, 1, 8, 0)
        }
    }

    private fun createPromotionMoves(piece: Piece, from: Position, to: Position, captured: Piece? = null): Set<Move> {
        return setOf(PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT).map { type ->
            Move(piece.player, from, to, piece, captured, promotionType = type)
        }.toSet()
    }
}
