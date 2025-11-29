package io.github.deanalvero.hcchess.bot

import io.github.deanalvero.hcchess.model.GameState
import io.github.deanalvero.hcchess.model.Move

interface BotStrategy {
    suspend fun calculateMove(gameState: GameState): Move?
}
