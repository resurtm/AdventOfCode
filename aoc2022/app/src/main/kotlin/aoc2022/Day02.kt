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

fun solveDay02() {
    drawSeparator()
    for (dft in DataFileType.values()) {
        solveDay02Internal(dft)
    }
}

private fun solveDay02Internal(dft: DataFileType) {
    var scorePart1 = 0
    var scorePart2 = 0
    File(getDataFilePath(2, dft)).forEachLine {
        if (it.isNotBlank()) {
            val its = it.split(" ")
            scorePart1 += scoresPart1[its[0]]?.get(its[1]) ?: 0
            scorePart2 += scoresPart2[its[0]]?.get(its[1]) ?: 0
        }
    }
    println("Part 1: $scorePart1. Part 2: $scorePart2.")
}

private val scoresPart1 = mapOf(
    // rock
    "A" to mapOf(
        "X" to 1 + 3, // rock
        "Y" to 2 + 6, // paper
        "Z" to 3 + 0, // scissor
    ),
    // paper
    "B" to mapOf(
        "X" to 1 + 0, // rock
        "Y" to 2 + 3, // paper
        "Z" to 3 + 6, // scissor
    ),
    // scissor
    "C" to mapOf(
        "X" to 1 + 6, // rock
        "Y" to 2 + 0, // paper
        "Z" to 3 + 3, // scissor
    ),
)

private val scoresPart2 = mapOf(
    // rock
    "A" to mapOf(
        "X" to 3 + 0, // lose
        "Y" to 1 + 3, // draw
        "Z" to 2 + 6, // win
    ),
    // paper
    "B" to mapOf(
        "X" to 1 + 0, // lose
        "Y" to 2 + 3, // draw
        "Z" to 3 + 6, // win
    ),
    // scissor
    "C" to mapOf(
        "X" to 2 + 0, // lose
        "Y" to 3 + 3, // draw
        "Z" to 1 + 6, // win
    ),
)
