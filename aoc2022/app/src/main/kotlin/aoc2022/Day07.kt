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
    private var rootNode = TreeNode(
        parent = null,
        name = "/",
        size = 0,
        type = TreeNodeType.DIR,
        dirs = mutableListOf(),
        files = mutableListOf()
    )
    private var currNode = rootNode

    fun parseLine(line: String) {
        if (line.first() == '$') parseCommand(line) else parseOutput(line)
    }

    private fun parseCommand(cmd: String) {
        val parts = cmd.trimStart('$', ' ').split(' ')
        when (parts[0]) {
            "cd" -> parseCommandCd(parts[1])
            "ls" -> {}
            else -> throw Exception("Cannot parse command: '$cmd'.")
        }
    }

    private fun parseCommandCd(dirName: String) {
        when (dirName) {
            "/" -> {
                currNode = rootNode
            }

            ".." -> {
                val parent = currNode.parent
                if (parent == null) throw Exception("Cannot move level up from root dir.")
                else currNode = parent
            }

            else -> {
                var subDir = currNode.dirs.find { it.name == dirName }
                if (subDir == null) subDir = addDir(dirName)
                currNode = subDir
            }
        }
    }

    private fun parseOutput(outLine: String) {
        val parts = outLine.split(' ')
        when (parts[0]) {
            "dir" -> addDir(parts[1])
            else -> addFile(parts[1], parts[0].toInt())
        }
    }

    private fun addDir(name: String): TreeNode {
        val newDir = TreeNode(
            parent = currNode,
            name = name,
            size = 0,
            type = TreeNodeType.DIR,
            dirs = mutableListOf(),
            files = mutableListOf()
        )
        currNode.dirs.add(newDir)
        return newDir
    }

    private fun addFile(name: String, size: Int): TreeNode {
        val newFile = TreeNode(
            parent = currNode,
            name = name,
            size = size,
            type = TreeNodeType.FILE,
            dirs = mutableListOf(),
            files = mutableListOf()
        )
        currNode.files.add(newFile)
        return newFile
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
