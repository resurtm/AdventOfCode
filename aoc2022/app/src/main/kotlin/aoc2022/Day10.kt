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

fun solveDay10() {
    Day10Solution(DataFileType.EXAMPLE).readFile().solve()
    Day10Solution(DataFileType.GOOGLE).readFile().solve()
}

private class Day10Solution(val dft: DataFileType) {
    private val instructions = mutableListOf<Instruction>()
    private var cycle = 0
    private var xVal = 1
    private var result = 0

    fun solve() {
        instructions.forEach {
            when (it.op) {
                OpType.NOOP -> incrementCycle()
                OpType.ADDX -> {
                    incrementCycle()
                    incrementCycle()
                    xVal += it.arg
                }
            }
        }
        println("Day 10. Type: ${dft}/${dft.value}. Part 1: $result.")
    }

    private fun incrementCycle() {
        cycle++
        if (cycle == 20 || cycle == 60 || cycle == 100 || cycle == 140 || cycle == 180 || cycle == 220) {
            result += cycle * xVal
        }
    }

    fun readFile(): Day10Solution {
        File(getDataFilePath(10, dft)).forEachLine { parseLine(it) }
        return this
    }

    private fun parseLine(inputLine: String) {
        val parts = inputLine.split(" ")
        when (parts[0]) {
            "noop" -> instructions.add(Instruction(OpType.NOOP))
            "addx" -> instructions.add(Instruction(OpType.ADDX, parts[1].toInt()))
            else -> throw Exception("Invalid instruction has been provided.")
        }
    }

    private data class Instruction(val op: OpType, val arg: Int = 0)

    private enum class OpType { NOOP, ADDX }
}
