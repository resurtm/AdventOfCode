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
    solveDay07Internal(DataFileType.GOOGLE)
}

private fun solveDay07Internal(dft: DataFileType) {
    File(getDataFilePath(7, dft)).forEachLine {
        DirTree.parseLine(it)
    }
    DirTree.printNodes()
    DirTree.findBigDirs()
    DirTree.findDeletable()
}

private enum class TreeNodeType(val type: String) {
    DIR("dir"),
    FILE("file")
}

private data class TreeNode(
    val parent: TreeNode? = null,
    val name: String,
    val size: Int = 0,
    var sizeTotal: Int? = null,
    val type: TreeNodeType,
    val dirs: MutableList<TreeNode> = mutableListOf(),
    val files: MutableList<TreeNode> = mutableListOf(),
    var include: Boolean = true
) {
    override fun toString(): String {
        val sizePart = when (type) {
            TreeNodeType.FILE -> ", size=$size"
            else -> ""
        }
        return "- $name (${type.type}$sizePart, total=${this.calcTotalSize()})"
    }

    fun toString(tab: Int) = "${" ".repeat(tab)}$this"

    fun calcTotalSize(): Int {
        if (!include) {
            sizeTotal = 0
            return 0
        }
        val result = calcTotalSizeInternal(this)
        sizeTotal = result
        return result
//        var result = sizeTotal
//        if (result == null) {
//            result = calcTotalSizeInternal(this)
//        }
//        sizeTotal = result
//        return result
    }

    private fun calcTotalSizeInternal(currNode: TreeNode): Int {
        var result = 0

        val fileSizes = currNode.files.map { it.size }
        result += if (fileSizes.isEmpty()) 0 else fileSizes.reduce { acc, size -> acc + size }

        val dirSizes = currNode.dirs.map { it.calcTotalSize() }
        result += if (dirSizes.isEmpty()) 0 else dirSizes.reduce { acc, size -> acc + size }

        return result
    }
}

private object DirTree {
    private var rootNode = TreeNode(name = "/", type = TreeNodeType.DIR)
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
        val newDir = TreeNode(parent = currNode, name = name, type = TreeNodeType.DIR)
        currNode.dirs.add(newDir)
        return newDir
    }

    private fun addFile(name: String, size: Int): TreeNode {
        val newFile = TreeNode(parent = currNode, name = name, size = size, type = TreeNodeType.FILE)
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

    fun findBigDirs() {
        val bigDirs = mutableListOf<TreeNode>()
        findBigDirsInternal(bigDirs, rootNode)
        println(bigDirs.map { it.calcTotalSize() }.reduce { acc, i -> acc + i })
    }

    private fun findBigDirsInternal(accum: MutableList<TreeNode>, node: TreeNode) {
        if (node.calcTotalSize() <= 100000) {
            accum.add(node)
        }
        node.dirs.forEach { findBigDirsInternal(accum, it) }
    }

    fun findDeletable() {
        println(rootNode.calcTotalSize())
        val accum = mutableListOf<TreeNode>()
        findDeletableInternal(accum, rootNode)
        accum.sortBy { it.calcTotalSize() }
        println(accum)

        var prev = rootNode
        for (it in accum) {
            it.include = false
            if (rootNode.calcTotalSize() < 70000000 - 30000000) {
                prev = it
                break
            }
            it.include = true
        }

        for (it in accum) {
            it.include = true
        }
        println(rootNode.calcTotalSize())
        println(prev.calcTotalSize())
    }

    private fun findDeletableInternal(accum: MutableList<TreeNode>, node: TreeNode) {
        accum.add(node)
        node.dirs.forEach { findDeletableInternal(accum, it) }
    }
}
