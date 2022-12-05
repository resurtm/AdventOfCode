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

fun solveDay03() {
    drawSeparator()
    for (dft in DataFileType.values()) {
        solveDay03Internal(dft)
    }
}

private fun solveDay03Internal(dft: DataFileType) {
    var resultPart1 = 0
    var resultPart2 = 0
    val accum = mutableListOf<String>()

    File(getDataFilePath(3, dft)).forEachLine {
        // part 1
        resultPart1 += lookup.getOrDefault(handleRucksack(it), 0)

        // part 2
        if (accum.size != 0 && accum.size % 3 == 0) {
            resultPart2 += lookup.getOrDefault(handleRucksacks(accum), 0)
        }
        accum.add(it)
    }
    resultPart2 += lookup.getOrDefault(handleRucksacks(accum), 0)

    println("Part 1: $resultPart1. Part 2: $resultPart2.")
}

private fun handleRucksacks(rucksacks: MutableList<String>): Char {
    val first = handleItems(rucksacks[0])
    val second = handleItems(rucksacks[1])
    val third = handleItems(rucksacks[2])
    rucksacks.clear()
    return first.intersect(second).intersect(third).first().first()
}

private fun handleItems(x: String) = x
    .split("")
    .filter { it.isNotBlank() }
    .toSet()

private fun handleRucksack(rucksack: String): Char {
    val first = handleItems(rucksack.substring(0, rucksack.length / 2))
    val second = handleItems(rucksack.substring(rucksack.length / 2, rucksack.length))
    return first.intersect(second).first().first()
}

private val lookup = (('a'..'z').zip(1..26) + ('A'..'Z').zip(27..52)).toMap()
