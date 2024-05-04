package com.mhowell

import com.mhowell.com.mhowell.wordsearch.service.WordSearchService

private fun createMatrix(): Array<CharArray> {
    return arrayOf(
        charArrayOf('A', 'I', 'D', 'N'),
        charArrayOf('N', 'E', 'N', 'Q'),
        charArrayOf('T', 'G', 'A', 'R'),
        charArrayOf('A', 'E', 'G', 'M')
    );
}

fun main() {
    val inputArray = createMatrix()
    val searchTerm = "MARNIE"

    println("Finding path for $searchTerm")
    val wordSearchService = WordSearchService()
    println(wordSearchService.search(inputArray, searchTerm))
}
