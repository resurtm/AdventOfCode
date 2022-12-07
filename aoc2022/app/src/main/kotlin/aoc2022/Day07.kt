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

fun solveDay07() {
    solveDay07Internal(DataFileType.EXAMPLE)
}

private fun solveDay07Internal(dft: DataFileType) {
    File(getDataFilePath(7, dft)).forEachLine {
        DirTree.parseLine(it)
    }
    DirTree.printNodes()
}

private enum class TreeNodeType(val type: String) {
    DIR("dir"),
    FILE("file")
}

private data class TreeNode(
    val parent: TreeNode?,
    val name: String,
    val size: Int,
    val type: TreeNodeType,
    val dirs: MutableList<TreeNode>,
    val files: MutableList<TreeNode>
) {
    override fun toString(): String {
        val sizePart = when (type) {
            TreeNodeType.FILE -> ", size=$size"
            else -> ""
        }
        return "- $name (${type.type}$sizePart)"
    }

    fun toString(tab: Int) = "${" ".repeat(tab)}$this"
}

private object DirTree {
    private fun getDefaultDir() = ArrayDeque(listOf("/"))
    private var currDir = getDefaultDir()
    private var rootNode = TreeNode(null, "/", 0, TreeNodeType.DIR, mutableListOf(), mutableListOf())
    private var currNode = rootNode

    fun parseLine(line: String) {
        if (line.first() == '$') parseCommand(line) else parseOutput(line)
    }

    private fun parseCommand(rawCmd: String) {
        val cmdParts = rawCmd.trimStart('$', ' ').split(' ')
        when (cmdParts.first()) {
            "cd" -> parseCommandCd(cmdParts.subList(1, cmdParts.size))
            "ls" -> parseCommandLs()
            else -> throw Exception("Cannot parse command: '$rawCmd'.")
        }
    }

    private fun parseCommandCd(parts: Collection<String>) {
        when (val dirName = parts.first()) {
            "/" -> {
                currDir = getDefaultDir()
                currNode = rootNode
            }

            ".." -> {
                currDir.removeLast()
                val parent = currNode.parent
                if (parent == null) throw Exception("Cannot move level up from root dir.")
                else currNode = parent
            }

            else -> {
                currDir.addLast(dirName)
                currNode.dirs.forEach {
                    if (it.name == dirName) {
                        currNode = it
                        return@forEach
                    }
                }
            }
        }
    }

    private fun parseCommandLs() {
        // do nothing
    }

    private fun parseOutput(rawOutLine: String) {
        val outLineParts = rawOutLine.split(' ')
        val rest = outLineParts.subList(1, outLineParts.size)
        when (outLineParts.first()) {
            "dir" -> parseOutputDir(rest)
            else -> parseOutputFile(outLineParts)
        }
    }

    private fun parseOutputDir(parts: Collection<String>) {
        currNode.dirs.add(
            TreeNode(currNode, parts.first(), 0, TreeNodeType.DIR, mutableListOf(), mutableListOf())
        )
    }

    private fun parseOutputFile(parts: List<String>) {
        currNode.files.add(
            TreeNode(currNode, parts[1], parts[0].toInt(), TreeNodeType.FILE, mutableListOf(), mutableListOf())
        )
    }

    fun printNodes() {
        printNode(rootNode)
    }

    private fun printNode(treeNode: TreeNode, tab: Int = 0) {
        println(treeNode.toString(tab))
        treeNode.dirs.forEach { printNode(it, tab + 2) }
        treeNode.files.forEach { printNode(it, tab + 2) }
    }
}
