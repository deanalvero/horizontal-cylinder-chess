package io.github.deanalvero.hcchess.model

import io.github.deanalvero.hcchess.utils.floorMod

data class Position(val file: Int, val rank: Int) {
    override fun toString(): String {
        val f = ('a' + file)
        val r = (rank + 1)
        return "$f$r"
    }

    fun plus(fileOffset: Int, rankOffset: Int): Position? {
        val newFile = file + fileOffset
        if (newFile !in 0..7) return null

        val newRank = floorMod(rank + rankOffset, 14)
        return Position(newFile, newRank)
    }
}