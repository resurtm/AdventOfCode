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

fun solveDay09() {
    solveDay09Internal(DataFileType.EXAMPLE)
    solveDay09Internal(DataFileType.GOOGLE)
    solveDay09Internal(DataFileType.GITHUB)
}

private fun solveDay09Internal(dft: DataFileType) {
    headX = 0
    headY = 0
    tailX = 0
    tailY = 0
    positionsPart1 = mutableSetOf<Pair<Int, Int>>()
    positionsPart2 = mutableSetOf<Pair<Int, Int>>()
    rope = MutableList(10) { Point(0, 0) }

    File(getDataFilePath(9, dft)).forEachLine {
        parseLine(it)
    }

    println(positionsPart1.size)
    println(positionsPart2.size)
}

private var headX = 0
private var headY = 0
private var tailX = 0
private var tailY = 0
private var positionsPart1 = mutableSetOf<Pair<Int, Int>>()
private var positionsPart2 = mutableSetOf<Pair<Int, Int>>()
private var rope = MutableList(10) { Point(0, 0) }

private data class Point(var x: Int, var y: Int)

private fun parseLine(line: String) {
    val parts = line.split(" ")
    repeat(parts[1].toInt()) {
        when (parts[0][0]) {
            'R' -> headX++
            'L' -> headX--
            'D' -> headY++
            'U' -> headY--
            else -> throw Exception("Cannot do the command")
        }

        val res = progressTail(headX, headY, tailX, tailY)
        tailX = res.x
        tailY = res.y

        rope[0] = Point(headX, headY)
        for (index in 0 until rope.size - 1) {
            rope[index + 1] = progressTail(rope[index].x, rope[index].y, rope[index + 1].x, rope[index + 1].y)
        }

        // positionsPart1.add(Pair(tailX, tailY))
        positionsPart1.add(Pair(rope[1].x, rope[1].y))

        positionsPart2.add(Pair(rope.last().x, rope.last().y))

//        printRope(-5, 5, -5, 5)
    }
}

private fun progressTail(headX: Int, headY: Int, tailX: Int, tailY: Int): Point {
    val result = Point(tailX, tailY)
    if (headX == tailX && abs(headY - tailY) == 2) {
        if (headY - tailY == 2) result.y++
        if (headY - tailY == -2) result.y--
        return result
    }
    if (abs(headX - tailX) == 2 && headY == tailY) {
        if (headX - tailX == 2) result.x++
        if (headX - tailX == -2) result.x--
        return result
    }
    if (abs(headX - tailX) == 1 && abs(headY - tailY) == 2) {
        result.x = headX
        if (headY - tailY == 2) result.y++
        if (headY - tailY == -2) result.y--
        return result
    }
    if (abs(headX - tailX) == 2 && abs(headY - tailY) == 1) {
        result.y = headY
        if (headX - tailX == 2) result.x++
        if (headX - tailX == -2) result.x--
        return result
    }
    if (abs(headX - tailX) == 2 && abs(headY - tailY) == 2) {
        result.x += (headX - tailX) / 2
        result.y += (headY - tailY) / 2
        return result
    }
    return result
}

private fun printRope(imin: Int, imax: Int, jmin: Int, jmax: Int) {
    for (j in jmin..jmax) {
        for (i in imin..imax) {
            var found = false
            if (!found && tailX == i && tailY == j) {
                print('T')
                found = true
            }
            if (!found && headX == i && headY == j) {
                print('H')
                found = true
            }
            rope.forEachIndexed { index, item ->
                if (!found && item.x == i && item.y == j) {
                    print(index)
                    found = true
                }
            }
            if (!found) {
                print('.')
            }
        }
        println()
    }
    println()
}
