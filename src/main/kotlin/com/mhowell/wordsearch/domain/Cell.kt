package com.mhowell.com.mhowell.wordsearch.domain

data class Cell(
    val subString: String,
    val x: Int,
    val y: Int,
    val path: List<Item> = mutableListOf(),
    val visited: Set<Pair<Int, Int>> = mutableSetOf(),
    val completed: Boolean = false
)

data class Item(val char: Char, val x: Int, val y: Int) {
    override fun toString(): String {
        return "$char ($x, $y)"
    }
}
