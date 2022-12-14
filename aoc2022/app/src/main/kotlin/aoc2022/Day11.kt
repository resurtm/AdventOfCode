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
    for (dft in DataFileType.values()) {
        val day11Solution = Day11Solution(dft)
        day11Solution.readFile()
        day11Solution.solve()
    }
}

private class Day11Solution(val dft: DataFileType) {
    private fun performOperation(operation: String, item: Int): Int {
        return if (operation.contains('*')) {
            val second = operation.split(" * ").last()
            if (second == "old") item * item else item * second.toInt()
        } else if (operation.contains('+')) {
            val second = operation.split(" + ").last()
            if (second == "old") item + item else item + second.toInt()
        } else {
            throw Exception("Unsupported item operation.")
        }
    }

    private fun processMonkey(monkey: Monkey) {
        while (monkey.items.isNotEmpty()) {
            val item = monkey.items.removeFirst()
            val afterOperation = performOperation(monkey.operation, item)
            val afterDiv = afterOperation / 3
            val nextMonkey = if (afterDiv % monkey.test == 0) monkey.ifTrue else monkey.ifFalse
            monkeys[nextMonkey].items.add(afterDiv)
            monkey.inspectedTimes++
        }
    }

    private fun runRound() = monkeys.forEach { it -> processMonkey(it) }

    fun solve() {
        repeat(20) { runRound() }
        monkeys.sortByDescending { it.inspectedTimes }
        val result = monkeys[0].inspectedTimes * monkeys[1].inspectedTimes
        println("Day 11. Data file type: ${dft}/${dft.value}. Part 1: ${result}.")
    }

    fun readFile() {
        File(getDataFilePath(11, dft)).forEachLine { parseLine(it) }
        monkeys.add(checkNotNull(currMonkey))
    }

    private fun parseLine(inputLine: String) {
        if (currMonkey == null) {
            currMonkey = Monkey()
        } else if (inputLine.length >= 2 && inputLine.substring(0, 2) != "  ") {
            monkeys.add(checkNotNull(currMonkey))
        }
        val ensured = checkNotNull(currMonkey)
        if (inputLine.contains("Starting items:")) {
            val items = inputLine.split(": ").last().split(", ").map { it.toInt() }
            currMonkey = ensured.copy(items = items.toMutableList())
        } else if (inputLine.contains("Operation:")) {
            currMonkey = ensured.copy(operation = inputLine.split(" = ").last())
        } else if (inputLine.contains("Test:")) {
            currMonkey = ensured.copy(test = inputLine.split(" by ").last().toInt())
        } else if (inputLine.contains("If true:")) {
            currMonkey = ensured.copy(ifTrue = inputLine.split(" to monkey ").last().toInt())
        } else if (inputLine.contains("If false:")) {
            currMonkey = ensured.copy(ifFalse = inputLine.split(" to monkey ").last().toInt())
        }
    }

    private var currMonkey: Monkey? = null
    private var monkeys = mutableListOf<Monkey>()

    private data class Monkey(
        val items: MutableList<Int> = mutableListOf(),
        val operation: String = "×",
        val test: Int = -1,
        val ifTrue: Int = -1,
        val ifFalse: Int = -1,
        var inspectedTimes: Int = 0
    )
}
