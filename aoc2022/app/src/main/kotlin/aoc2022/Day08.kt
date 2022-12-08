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

fun solveDay08() {
    solveDay08Internal(DataFileType.GOOGLE)
}

private fun solveDay08Internal(dft: DataFileType) {
    File(getDataFilePath(8, dft)).forEachLine {
        readLine(it)
    }

    sizeI = grid[0].size
    sizeJ = grid.size

    for (j in 0 until sizeJ) {
        var max = '/'
        for (i in 0 until sizeI) {
            if (max < grid[j][i].char) {
                max = grid[j][i].char
                grid[j][i].visible = true
            }
        }
        max = '/'
        for (i in sizeI - 1 downTo 0) {
            if (max < grid[j][i].char) {
                max = grid[j][i].char
                grid[j][i].visible = true
            }
        }
    }
    for (i in 0 until sizeI) {
        var max = '/'
        for (j in 0 until sizeJ) {
            if (max < grid[j][i].char) {
                max = grid[j][i].char
                grid[j][i].visible = true
            }
        }
        max = '/'
        for (j in sizeJ - 1 downTo 0) {
            if (max < grid[j][i].char) {
                max = grid[j][i].char
                grid[j][i].visible = true
            }
        }
    }

    var count = 0
    for (j in 0 until sizeJ) {
        for (i in 0 until sizeI) {
//            print(if (grid[j][i].visible) '1' else '0')
            if (grid[j][i].visible) {
                count++
            }
        }
//        println()
    }
    println(count)
}

private data class Tree(val char: Char, var visible: Boolean)

private var sizeI = 0
private var sizeJ = 0
private val grid = mutableListOf<MutableList<Tree>>()

private fun readLine(line: String) {
    val row = mutableListOf<Tree>()
    line.forEach { row.add(Tree(it, false)) }
    grid.add(row)
}

