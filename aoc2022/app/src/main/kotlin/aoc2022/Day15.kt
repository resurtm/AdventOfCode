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

fun solveDay15() {
    val day15Solution = Day15Solution(DataFileType.EXAMPLE)
    day15Solution.readFile()
}

private class Day15Solution(val dft: DataFileType) {
    fun readFile() {
        File(getDataFilePath(15, dft)).forEachLine { parseLine(it) }
    }

    private fun parseLine(inputLine: String) {
        val matchResult = lineRegex.matchEntire(inputLine) ?: return

        val sensorX = matchResult.groups[1]?.value?.toInt() ?: return
        val sensorY = matchResult.groups[2]?.value?.toInt() ?: return

        val closestBeaconX = matchResult.groups[3]?.value?.toInt() ?: return
        val closestBeaconY = matchResult.groups[4]?.value?.toInt() ?: return

        val sensor = Sensor(sensor = Point(sensorX, sensorY), closestBeacon = Point(closestBeaconX, closestBeaconY))
        sensors = sensors.plus(sensor)
    }

    private var sensors = emptyList<Sensor>()
    private val lineRegex = Regex(
        "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)"
    )

    private data class Sensor(val sensor: Point, val closestBeacon: Point)
    private data class Point(val x: Int = 0, val y: Int = 0)
}
