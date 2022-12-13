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

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type

fun solveDay13() {
    Day13Solution(DataFileType.EXAMPLE).readFile().solvePart1().solvePart2()
    Day13Solution(DataFileType.GITHUB).readFile().solvePart1().solvePart2()
    Day13Solution(DataFileType.GOOGLE).readFile().solvePart1().solvePart2()
}

private class Day13Solution(val dft: DataFileType) {
    fun solvePart1(): Day13Solution {
        var result = 0
        lists.forEachIndexed { index, pair ->
            if (checkPair(pair) == true) result += index + 1
        }
        println("Day 13. Type: $dft/${dft.value}. Part 1: $result.")
        return this
    }

    fun solvePart2() {
        val newP1 = listOf(ListItem(vector = listOf(ListItem(scalar = 2))))
        val newP2 = listOf(ListItem(vector = listOf(ListItem(scalar = 6))))

        var newPos1 = -1
        var newPos2 = -1

        rawLists.add(newP1)
        rawLists.add(newP2)
        rawLists.sortWith(Day13Solution)
        rawLists.forEachIndexed { index, lst ->
            if (lst == newP1) newPos1 = index + 1
            if (lst == newP2) newPos2 = index + 1
        }
        println("Day 13. Type: $dft/${dft.value}. Part 2: ${newPos1 * newPos2}.")
    }

    fun readFile(): Day13Solution {
        File(getDataFilePath(13, dft)).forEachLine { parseLine(it) }
        return this
    }

    private fun parseLine(inputLine: String) {
        val rawParsed = parseLineWithList(inputLine)
        if (rawParsed != null) rawLists.add(rawParsed)

        when (readState) {
            ReadState.FIRST_LIST -> {
                firstList = parseLineWithList(inputLine)
                readState = ReadState.SECOND_LIST
            }

            ReadState.SECOND_LIST -> {
                secondList = parseLineWithList(inputLine)
                if (firstList != null && secondList != null) {
                    lists.add(Pair(firstList as List<ListItem>, secondList as List<ListItem>))
                }
                readState = ReadState.SEPARATOR
            }

            ReadState.SEPARATOR -> {
                readState = ReadState.FIRST_LIST
            }
        }
    }

    private fun parseLineWithList(inputLineWithList: String): List<ListItem>? {
        val gson = GsonBuilder()
            .registerTypeAdapter(ListItem::class.java, ListItemSerializer())
            .create()
        val typeTok = object : TypeToken<List<ListItem>>() {}.type
        return gson.fromJson<List<ListItem>>(inputLineWithList, typeTok)
    }

    private val lists = mutableListOf<Pair<List<ListItem>, List<ListItem>>>()
    private val rawLists = mutableListOf<List<ListItem>>()

    private var readState = ReadState.FIRST_LIST
    private var firstList: List<ListItem>? = null
    private var secondList: List<ListItem>? = null

    data class ListItem(
        val scalar: Int? = null,
        val vector: List<ListItem>? = null
    )

    private class ListItemSerializer : JsonDeserializer<ListItem> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ListItem {
            if (json?.isJsonPrimitive == true) {
                return ListItem(scalar = json.asJsonPrimitive.asInt)
            }
            if (json?.isJsonArray == true) {
                val typeTok = object : TypeToken<List<ListItem>>() {}.type
                val result = context?.deserialize<List<ListItem>>(json.asJsonArray, typeTok)
                return ListItem(vector = result)
            }
            throw Exception("Invalid input data has been provided.")
        }
    }

    private enum class ReadState { FIRST_LIST, SECOND_LIST, SEPARATOR }

    private companion object : Comparator<List<ListItem>> {
        override fun compare(a: List<ListItem>, b: List<ListItem>): Int {
            return when (checkPair(Pair(a, b))) {
                true -> -1
                false -> 1
                null -> 0
            }
        }
    }
}

private fun checkPair(pair: Pair<List<Day13Solution.ListItem>, List<Day13Solution.ListItem>>): Boolean? {
    var index = 0
    while (true) {
        val first = pair.first.getOrNull(index)
        val second = pair.second.getOrNull(index++)
        if (first == null && second != null) {
            return true
        }
        if (second == null && first != null) {
            return false
        }
        if (second == null || first == null) {
            break
        }

        if (first.scalar != null && second.scalar != null) {
            if (first.scalar < second.scalar) return true
            if (first.scalar > second.scalar) return false
        } else {
            val newFirst = first.vector ?: listOf(Day13Solution.ListItem(scalar = first.scalar))
            val newSecond = second.vector ?: listOf(Day13Solution.ListItem(scalar = second.scalar))
            val result = checkPair(Pair(newFirst, newSecond))
            if (result != null) return result
        }
    }
    return null
}
