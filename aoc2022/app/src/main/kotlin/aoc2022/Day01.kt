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

fun solveDay01() {
    drawSeparator()
    for (dft in DataFileType.values()) {
        solveDay01Internal(dft)
    }
}

private fun solveDay01Internal(dft: DataFileType) {
    var sum = 0
    val maxes = mutableListOf(0, 0, 0)

    fun processElf() {
        for ((index, value) in maxes.withIndex()) {
            if (sum > value) {
                maxes[index] = sum
                break
            }
        }
        maxes.sort()
        sum = 0
    }

    File(getDataFilePath(1, dft)).forEachLine {
        if (it.isBlank()) {
            processElf()
        } else {
            sum += it.toInt()
        }
    }
    processElf()

    val maxesSum = maxes.reduce { acc, next -> acc + next }
    println("Calories: ${maxes.last()}. Sum: $maxesSum.")
}
