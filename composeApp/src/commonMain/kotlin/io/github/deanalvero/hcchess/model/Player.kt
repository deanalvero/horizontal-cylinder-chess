package io.github.deanalvero.hcchess.model

sealed class Player(val id: String) {
    object WHITE : Player("White")
    object BLACK : Player("Black")

    val opponent: Player
        get() = when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
}
