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

fun solveDay12() {
    Day12Solution().readFile(DataFileType.EXAMPLE).solve()
    Day12Solution().readFile(DataFileType.GITHUB).solve()
    Day12Solution().readFile(DataFileType.GOOGLE).solve()
}

private class Day12Solution {
    data class Vector(val x: Int = 0, val y: Int = 0) {
        override fun toString(): String = "${x}x${y}"
    }

    data class Node(
        val ch: Char,
        val pos: Vector = Vector(),
        var visited: Boolean = false,
        var tentative: Int = Int.MAX_VALUE - 1,
        var parent: Node? = null
    )

    private var grid = mutableListOf<MutableList<Node>>()
    private var gridSize = Vector()

    private var startPos = Vector()
    private var endPos = Vector()

    fun solve() {
        var lastNode1 = followPath(null)
        var size1 = 0
        while (lastNode1.parent != null) {
            size1++
            val nextNode = lastNode1.parent
            if (nextNode != null) lastNode1 = nextNode
        }

        var lastNode2 = followPath('a')
        var size2 = 0
        while (lastNode2.parent != null) {
            size2++
            val nextNode = lastNode2.parent
            if (nextNode != null) lastNode2 = nextNode
        }

        println("Day 12. Part 1: $size1. Part 2: $size2.")
    }

    private fun followPath(lookFor: Char?): Node {
        grid.forEach { row -> row.forEach { it.visited = false } }

        val queue = mutableListOf<Node>()

        val rootNode = grid[endPos.y][endPos.x]
        rootNode.visited = true
        queue.add(rootNode)

        while (queue.isNotEmpty()) {
            val curr = queue.removeFirst()
            if (lookFor == null && curr.pos == startPos) {
                return curr
            }
            if (lookFor == curr.ch) {
                return curr
            }

            for (node in adjacentNodes(curr)) {
                if (node.visited) continue
                node.visited = true
                node.parent = curr
                queue.add(node)
            }
        }

        throw Exception("This should not happen")
    }

    private fun adjacentNodes(node: Node): List<Node> {
        val result = mutableListOf<Node>()
        if (node.pos.x > 0 && canMove(node.pos, Vector(node.pos.x - 1, node.pos.y))) {
            result.add(grid[node.pos.y][node.pos.x - 1])
        }
        if (node.pos.x < gridSize.x - 1 && canMove(node.pos, Vector(node.pos.x + 1, node.pos.y))) {
            result.add(grid[node.pos.y][node.pos.x + 1])
        }
        if (node.pos.y > 0 && canMove(node.pos, Vector(node.pos.x, node.pos.y - 1))) {
            result.add(grid[node.pos.y - 1][node.pos.x])
        }
        if (node.pos.y < gridSize.y - 1 && canMove(node.pos, Vector(node.pos.x, node.pos.y + 1))) {
            result.add(grid[node.pos.y + 1][node.pos.x])
        }
        return result
    }

    private fun canMove(pos2: Vector, pos1: Vector): Boolean {
        val diff = grid[pos2.y][pos2.x].ch - grid[pos1.y][pos1.x].ch
        return diff <= 1
    }

    fun readFile(dft: DataFileType): Day12Solution {
        var j = 0
        File(getDataFilePath(12, dft)).forEachLine { parseLine(it, j++) }
        gridSize = Vector(grid[0].size, grid.size)
        return this
    }

    private fun parseLine(line: String, j: Int) {
        val row = mutableListOf<Node>()
        line.forEachIndexed { index, ch ->
            when (ch) {
                'S' -> {
                    startPos = Vector(index, j)
                    row.add(Node('a', Vector(index, j), tentative = 0))
                }

                'E' -> {
                    endPos = Vector(index, j)
                    row.add(Node('z', Vector(index, j)))
                }

                else -> row.add(Node(ch, Vector(index, j)))
            }
        }
        grid.add(row)
    }
}
