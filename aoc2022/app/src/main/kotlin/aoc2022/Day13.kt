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
    Day13Solution().readFile(DataFileType.EXAMPLE).solve()
//    Day13Solution().readFile(DataFileType.GITHUB)
//    Day13Solution().readFile(DataFileType.GOOGLE)
}

private class Day13Solution {
    fun solve() {
        println(lists[1].first)
        println(lists[1].second)
    }

    fun readFile(dft: DataFileType): Day13Solution {
        File(getDataFilePath(13, dft)).forEachLine { parseLine(it) }
        return this
    }

    private fun parseLine(inputLine: String) = when (readState) {
        ReadState.FIRST_LIST -> {
            firstList = parseLineWithList(inputLine)
            readState = ReadState.SECOND_LIST
        }

        ReadState.SECOND_LIST -> {
            secondList = parseLineWithList(inputLine)
            readState = ReadState.SEPARATOR
        }

        ReadState.SEPARATOR -> {
            if (firstList != null && secondList != null) {
                lists.add(Pair(firstList as List<ListItem>, secondList as List<ListItem>))
            }
            firstList = null
            secondList = null
            readState = ReadState.FIRST_LIST
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

    private var readState = ReadState.FIRST_LIST
    private var firstList: List<ListItem>? = null
    private var secondList: List<ListItem>? = null

    private data class ListItem(
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
}
