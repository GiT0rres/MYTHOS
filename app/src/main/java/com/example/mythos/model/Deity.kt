package com.example.mythos.model

/**
 * Representa uma obra do acervo: um deus ou herói e sua representação artística.
 */
data class Deity(
    val id: String = "",
    val name: String = "",
    val culture: String = "",
    val period: String = "",
    val epithet: String = "",
    val description: String = "",
    val power: String = "",
    val symbol: String = "",
    val domain: String = "",
    val artwork: String = ""
)
