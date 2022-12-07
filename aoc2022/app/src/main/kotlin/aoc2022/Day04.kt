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

fun solveDay04() {
    for (dft in DataFileType.values()) {
        solveDay04Internal(dft)
    }
}

private fun solveDay04Internal(dft: DataFileType) {
    var resultPart1 = 0
    var resultPart2 = 0
    File(getDataFilePath(4, dft)).forEachLine {
        resultPart1 += if (handlePair(it).first) 1 else 0
        resultPart2 += if (handlePair(it).second) 1 else 0
    }
    println("Part 1: $resultPart1. Part 2: $resultPart2.")
}

private fun handleInterval(interval: String): IntRange {
    val raw = interval.split("-").map { it.toInt() }
    return raw[0]..raw[1]
}

private fun handlePair(pair: String): Pair<Boolean, Boolean> {
    val first = handleInterval(pair.split(",")[0])
    val second = handleInterval(pair.split(",")[1])

    val all = first.all { second.contains(it) } || second.all { first.contains(it) }
    val any = first.any { second.contains(it) } || second.any { first.contains(it) }

    return Pair(all, any)
}
