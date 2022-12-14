// MIT License
//
// Copyright (c) 2022 resurtm@gmail.com
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package aoc2022

import java.io.File

fun solveDay14() {
    val day14Solution = Day14Solution(DataFileType.EXAMPLE)
    day14Solution.readFile()
    day14Solution.renderMap()
}

private class Day14Solution(val dft: DataFileType) {
    fun renderMap() {
        for (y in gridSize.first.y..gridSize.second.y) {
            for (x in gridSize.first.x..gridSize.second.x) {
                print(grid[x][y].type.ch)
            }
            println()
        }
    }

    fun readFile() = File(getDataFilePath(14, dft)).forEachLine { parseLine(it) }

    private fun parseLine(inputLine: String) {
        var prevPoint: Point? = null
        inputLine.split(" -> ").forEach { rawCoords ->
            val coords = rawCoords.split(',')
            val point = Point(coords.first().toInt(), coords.last().toInt())
            ensureGridSize(point)
            prevPoint?.let { fillGrid(it, point) }
            prevPoint = point
        }
    }

    private fun fillGrid(start: Point, end: Point) =
        if (start.x == end.x && start.y != end.y) fillGridVertical(start.x, start.y, end.y)
        else if (start.y == end.y && start.x != end.x) fillGridHorizontal(start.y, start.x, end.x)
        else throw Exception("This kind of fill is not supported.")

    private fun fillGridVertical(x: Int, startY: Int, endY: Int) {
        val y1 = if (startY < endY) startY else endY
        val y2 = if (startY < endY) endY else startY
        for (y in y1..y2) grid[x][y].type = NodeType.ROCK
    }

    private fun fillGridHorizontal(y: Int, startX: Int, endX: Int) {
        val x1 = if (startX < endX) startX else endX
        val x2 = if (startX < endX) endX else startX
        for (x in x1..x2) grid[x][y].type = NodeType.ROCK
    }

    private fun ensureGridSize(p: Point) {
        var min = gridSize.first
        var max = gridSize.second
        if (p.x < min.x) min = min.copy(x = p.x)
        if (p.y < min.y) min = min.copy(y = p.y)
        if (p.x > max.x) max = max.copy(x = p.x)
        if (p.y > max.y) max = max.copy(y = p.y)
        gridSize = Pair(min, max)
    }

    private val grid = List(1000) { List(1000) { Node(NodeType.AIR) } }
    private var gridSize = Pair(Point(Int.MAX_VALUE, Int.MAX_VALUE), Point(Int.MIN_VALUE, Int.MIN_VALUE))

    private data class Node(var type: NodeType)
    private enum class NodeType(val ch: Char) { ROCK('#'), AIR('.'), SAND('+') }
    private data class Point(val x: Int = 0, val y: Int = 0) {
        override fun toString(): String = "${x}×${y}"
    }
}
