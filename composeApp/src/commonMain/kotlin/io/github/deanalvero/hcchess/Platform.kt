package io.github.deanalvero.hcchess

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform