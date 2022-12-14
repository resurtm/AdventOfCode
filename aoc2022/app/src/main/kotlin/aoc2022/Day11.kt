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

fun solveDay11() {
    val day11Solution = Day11Solution(DataFileType.EXAMPLE)
    day11Solution.readFile()
    day11Solution.solve()
}

private class Day11Solution(val dft: DataFileType) {
    fun solve() = monkeys.forEach { println(it) }

    fun readFile() {
        File(getDataFilePath(11, dft)).forEachLine { parseLine(it) }
        monkeys = monkeys.plus(checkNotNull(currMonkey))
    }

    private fun parseLine(inputLine: String) {
        if (currMonkey == null) {
            currMonkey = Monkey()
        } else if (inputLine.length >= 2 && inputLine.substring(0, 2) != "  ") {
            monkeys = monkeys.plus(checkNotNull(currMonkey))
        }
        if (inputLine.contains("Starting items:")) {
            val items = inputLine.split(": ").last().split(", ").map { it.toInt() }
            currMonkey = checkNotNull(currMonkey).copy(items = items)
        } else if (inputLine.contains("Operation:")) {
            currMonkey = checkNotNull(currMonkey).copy(operation = inputLine.split(" = ").last())
        } else if (inputLine.contains("Test:")) {
            currMonkey = checkNotNull(currMonkey).copy(test = inputLine.split(" by ").last().toInt())
        } else if (inputLine.contains("If true:")) {
            currMonkey = checkNotNull(currMonkey).copy(ifTrue = inputLine.split(" to monkey ").last())
        } else if (inputLine.contains("If false:")) {
            currMonkey = checkNotNull(currMonkey).copy(ifFalse = inputLine.split(" to monkey ").last())
        }
    }

    private var currMonkey: Monkey? = null
    private var monkeys = emptyList<Monkey>()

    private data class Monkey(
        val items: List<Int> = emptyList(),
        val operation: String = "",
        val test: Int = 0,
        val ifTrue: String = "",
        val ifFalse: String = ""
    )
}
