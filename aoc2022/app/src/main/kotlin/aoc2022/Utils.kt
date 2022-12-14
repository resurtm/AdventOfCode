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

import java.nio.file.Paths

enum class DataFileType(val value: String) {
    EXAMPLE("test"),
    GITHUB("gh"),
    GOOGLE("google")
}

fun getDataFilePath(dayCount: Short, dft: DataFileType): String {
    val absPath = Paths.get("").toAbsolutePath().toString()
    return "$absPath/aoc2022/data/day${dayCount.toString().padStart(2, '0')}/input-${dft.value}.txt"
}

fun gcd(aInp: Long, bInp: Long): Long {
    var a = aInp
    var b = bInp
    while (b > 0) {
        val temp = b
        b = a % b // % is remainder
        a = temp
    }
    return a
}

fun gcd(input: LongArray): Long {
    var result = input[0]
    for (i in 1 until input.size) result = gcd(result, input[i])
    return result
}

fun lcm(a: Long, b: Long): Long = a * (b / gcd(a, b))

fun lcm(input: LongArray): Long {
    var result = input[0]
    for (i in 1 until input.size) result = lcm(result, input[i])
    return result
}
