package com.mhowell.com.mhowell.wordsearch.service

import com.mhowell.com.mhowell.wordsearch.domain.Cell
import com.mhowell.com.mhowell.wordsearch.domain.Item
import java.util.LinkedList


class WordSearchService {

    /**
     * Given a matrix of characters and a search term, return the first (if any) path from any staring character
     * in the matrix that is the shortest path to find the search term. Diagonals are allowed. Traversing the same index
     * is not.
     *
     * @param matrix 2d char array
     * @param searchTerm term to search for
     * @return If found, a list of the shortest path found in the matrix for the search term
     */
    fun search(matrix: Array<CharArray>, searchTerm: String): List<Item> {

        val queue = LinkedList<List<Cell>>()
        queue.addAll(findPotentialStarts(matrix, searchTerm[0]))

        while (!queue.isEmpty()) {
            val currentSearch = queue.pop()
            val remainingSearch = searchTerm.substringAfter(currentSearch.last().subString)
            if (remainingSearch.isEmpty()) {
                continue
            }

            val nextSearches = search(matrix, searchTerm, remainingSearch[0], currentSearch)
            if (nextSearches.isEmpty()) {
                continue
            }

            if (!nextSearches.map { it.last() }.filter { it.completed }.any { return it.path }) {
                nextSearches.forEach { queue.add(it) }
            }
        }
        return emptyList()
    }

    private fun search(
        matrix: Array<CharArray>, searchTerm: String, nextChar: Char, cells: List<Cell>
    ): List<List<Cell>> {
        val cell = cells.last()
        val coordinatesCandidates = newSearchCoordinates(matrix, cell, cell.x, cell.y)
        if (coordinatesCandidates.isEmpty()) {
            println("No possible continuation for $cells as out of bounds")
            return emptyList()
        }

        return findPossibleCandidates(matrix, searchTerm, nextChar, coordinatesCandidates, cells)
    }

    private fun newSearchCoordinates(matrix: Array<CharArray>, cell: Cell, x: Int, y: Int): Set<Pair<Int, Int>> {
        val pairs = mutableSetOf<Pair<Int, Int>>()

        val rows = matrix.size
        val cols = matrix[0].size

        // Horizontal/vertical mapping
        // -X-
        // XYX-
        // -X-
        // LEFT
        if (x - 1 >= 0) {
            pairs.add(Pair(x - 1, y))
        }
        // RIGHT
        if (x + 1 < rows) {
            pairs.add(Pair(x + 1, y))
        }
        // UP
        if (y - 1 >= 0) {
            pairs.add(Pair(x, y - 1))
        }
        // DOWN
        if (y + 1 < cols) {
            pairs.add(Pair(x, y + 1))
        }
        // Diagonal mapping
        // X-X
        // -Y-
        // X-X
        // UPPER LEFT
        if (x - 1 >= 0 && y - 1 >= 0) {
            pairs.add(Pair(x - 1, y - 1))
        }
        // UPPER RIGHT
        if (x - 1 >= 0 && y + 1 < cols) {
            pairs.add(Pair(x - 1, y + 1))
        }
        // LOWER LEFT
        if (x + 1 < rows && y - 1 >= 0) {
            pairs.add(Pair(x + 1, y - 1))
        }
        // LOWER RIGHT
        if (x + 1 < rows && y + 1 < cols) {
            pairs.add(Pair(x + 1, y + 1))
        }

        return pairs.filter { !cell.visited.contains(it) }.toSet()
    }

    private fun findPossibleCandidates(
        matrix: Array<CharArray>,
        searchTerm: String,
        nextChar: Char,
        coordinateCandidates: Set<Pair<Int, Int>>,
        cells: List<Cell>
    ): List<List<Cell>> {
        return coordinateCandidates
            .map { checkCandidate(matrix, searchTerm, cells, nextChar, it.first, it.second) }
            .filter { it.isNotEmpty() }
            .toList()
    }

    private fun checkCandidate(
        matrix: Array<CharArray>,
        searchTerm: String,
        cells: List<Cell>,
        nextChar: Char,
        x: Int,
        y: Int
    ): List<Cell> {
        val cell = cells.last()
        val visitedCells = cell.visited

        if (matrix[x][y] == nextChar && !visitedCells.contains(Pair(x, y))) {
            val newCells = cells.toMutableList()
            val newSubstring = "${cell.subString}${nextChar}"
            val newVisited = visitedCells.toMutableSet()
            newVisited.add(Pair(x, y))
            val newCell = Cell(
                subString = newSubstring,
                x = x,
                y = y,
                completed = newSubstring == searchTerm,
                path = cell.path + Item(nextChar, x, y),
                visited = newVisited
            )
            newCells.add(newCell)
            return newCells
        }

        return emptyList()
    }

    private fun findPotentialStarts(matrix: Array<CharArray>, char: Char): List<List<Cell>> {
        val potentialStarts = mutableListOf<Cell>()
        matrix.forEachIndexed { x, r ->
            run {
                r.forEachIndexed { y, c ->
                    run {
                        if (c == char) {
                            potentialStarts.add(
                                Cell(
                                    subString = c.toString(),
                                    x = x,
                                    y = y,
                                    path = mutableListOf(Item(c, x, y)),
                                    visited = mutableSetOf(
                                        Pair(x, y)
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
        return potentialStarts.map { mutableListOf(it) }
    }
}
