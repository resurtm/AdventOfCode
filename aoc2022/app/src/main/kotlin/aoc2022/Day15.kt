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
import kotlin.math.abs

fun solveDay15() {
    val day15Solution1 = Day15Solution(DataFileType.EXAMPLE)
    day15Solution1.readFile()
    day15Solution1.solvePart1(10)

    val day15Solution2 = Day15Solution(DataFileType.GITHUB)
    day15Solution2.readFile()
    day15Solution2.solvePart1(2000000)

    val day15Solution3 = Day15Solution(DataFileType.GOOGLE)
    day15Solution3.readFile()
    day15Solution3.solvePart1(2000000)
}

private class Day15Solution(val dft: DataFileType) {
    fun solvePart1(neededY: Int) {
        sensors.forEach { sensor ->
            calcInterval(sensor, neededY)?.let { intervals.add(it) }
        }
        val merged = mergedIntervals()
        val result = merged.first().second - merged.first().first
        println("Day 15. Data file type: ${dft}/${dft.value}. Part 1: $result.")
    }

    private fun mergedIntervals(): MutableList<Pair<Int, Int>> {
        val intvals = intervals.sortedWith(IntervalComparator)
        val merged = mutableListOf(intvals[0].copy())
        for (interval in intvals) {
            val last = merged.last()
            if (
                last.first <= interval.first &&
                last.second >= interval.first &&
                last.second <= interval.second
            ) {
                merged[merged.size - 1] = Pair(last.first, interval.second)
            } else if (
                last.first <= interval.first &&
                last.second >= interval.second
            ) {
                // do nothing
            } else if (
                last.first >= interval.first &&
                last.second <= interval.second
            ) {
                merged[merged.size - 1] = interval.copy()
            } else {
                merged.add(interval.copy())
            }
        }
        return merged
    }

    private fun calcInterval(sen: Sensor, neededY: Int): Pair<Int, Int>? {
        val coverage = abs(sen.pos.x - sen.beacon.x) + abs(sen.pos.y - sen.beacon.y)
        val yDist = abs(sen.pos.y - neededY)
        if (yDist > coverage) return null
        return Pair(sen.pos.x - (coverage - yDist), sen.pos.x + (coverage - yDist))
    }

    fun readFile() = File(getDataFilePath(15, dft)).forEachLine { parseLine(it) }

    private fun parseLine(inputLine: String) {
        val matchResult = lineRegex.matchEntire(inputLine) ?: return

        val sensorX = matchResult.groups[1]?.value?.toInt() ?: return
        val sensorY = matchResult.groups[2]?.value?.toInt() ?: return

        val closestBeaconX = matchResult.groups[3]?.value?.toInt() ?: return
        val closestBeaconY = matchResult.groups[4]?.value?.toInt() ?: return

        val sensor = Sensor(pos = Point(sensorX, sensorY), beacon = Point(closestBeaconX, closestBeaconY))
        sensors = sensors.plus(sensor)
    }

    private var sensors = emptyList<Sensor>()
    private val intervals = mutableListOf<Pair<Int, Int>>()
    private val lineRegex = Regex(
        "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)"
    )

    private data class Sensor(val pos: Point, val beacon: Point)
    private data class Point(val x: Int = 0, val y: Int = 0)

    private class IntervalComparator {
        companion object : Comparator<Pair<Int, Int>> {
            override fun compare(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
                val second = if (a.second == b.second) 0
                else if (a.second < b.second) -1
                else 1
                return if (a.first == b.first) second
                else if (a.first < b.first) -1
                else 1
            }
        }
    }
}
