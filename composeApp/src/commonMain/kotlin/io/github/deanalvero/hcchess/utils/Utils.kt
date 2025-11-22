package io.github.deanalvero.hcchess.utils

fun floorMod(a: Int, b: Int): Int {
    return ((a % b) + b) % b
}
