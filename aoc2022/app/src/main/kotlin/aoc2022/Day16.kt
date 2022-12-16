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

fun solveDay16() {
    val day16Solution1 = Day16Solution(DataFileType.EXAMPLE)
    day16Solution1.readFile()
    day16Solution1.solve()

//    val day16Solution2 = Day16Solution(DataFileType.GITHUB)
//    day16Solution2.readFile()
//    day16Solution2.solve()
}

private class Day16Solution(val dft: DataFileType) {
    fun solve() = valves.forEach { println(it) }

    fun readFile() = File(getDataFilePath(16, dft)).forEachLine { parseLine(it) }

    private fun parseLine(inputLine: String) {
        val matchResult = lineRegex.matchEntire(inputLine) ?: return
        val name = matchResult.groups[1]?.value ?: return
        val rate = matchResult.groups[2]?.value?.toInt() ?: return
        val rawNext = matchResult.groups[3]?.value ?: return
        val next = rawNext.trim().split(", ")
        valves = valves.plus(Valve(name, rate, next))
    }

    private var valves = emptyList<Valve>()
    private val lineRegex = Regex("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? (.*)")

    private data class Valve(val name: String, val flow: Int, val next: List<String>)
}
