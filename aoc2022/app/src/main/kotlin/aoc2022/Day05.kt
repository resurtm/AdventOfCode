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

fun solveDay05() {
    drawSeparator()
    for (dft in DataFileType.values()) {
        solveDay05Internal(dft)
    }
}

private fun solveDay05Internal(dft: DataFileType) {
    val filePath = getDataFilePath(5, dft)
    val part1 = Day05Solution().readFile(filePath, true).genMessage()
    val part2 = Day05Solution().readFile(filePath, false).genMessage()
    println("Part 1: $part1. Part 2: $part2.")
}

private class Day05Solution {
    private var colsRead = true
    private var cols = mutableListOf<MutableList<Char>>()
    private val instrRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()

    fun readFile(filePath: String, simpleMode: Boolean): Day05Solution {
        File(filePath).forEachLine {
            if (colsRead) {
                readRow("$it ")
            } else {
                readInstr(it, simpleMode)
            }
        }
        return this
    }

    fun genMessage(): String {
        var result = ""
        cols.forEach { result += it[0] }
        return result
    }

    private fun readRow(row: String) {
        if (cols.isEmpty()) {
            cols = MutableList(row.length / 4) { mutableListOf() }
        }
        for (index in row.indices step 4) {
            val ch = row.substring(index, index + 4)[1]
            if ('1' == ch) {
                colsRead = false
                return
            }
            if (ch != ' ') {
                cols[index / 4].add(ch)
            }
        }
    }

    private fun readInstr(instr: String, simpleMode: Boolean) {
        val matchResult = instrRegex.matchEntire(instr) ?: return
        val amount = matchResult.groups[1]?.value?.toInt() ?: return
        val from = matchResult.groups[2]?.value?.toInt() ?: return
        val to = matchResult.groups[3]?.value?.toInt() ?: return
        if (simpleMode) {
            readInstrSimple(amount, from, to)
        } else {
            readInstrComplex(amount, from, to)
        }
    }

    private fun readInstrSimple(amount: Int, from: Int, to: Int) {
        repeat(amount) {
            val item = cols[from - 1].removeAt(0)
            cols[to - 1].add(0, item)
        }
    }

    private fun readInstrComplex(amount: Int, from: Int, to: Int) {
        if (amount == 1) {
            readInstrSimple(amount, from, to)
            return
        }
        val items = mutableListOf<Char>()
        repeat(amount) {
            items.add(cols[from - 1].removeAt(0))
        }
        cols[to - 1].addAll(0, items)
    }
}
