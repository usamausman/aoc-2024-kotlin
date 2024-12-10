package day9

import utils.AOC
import kotlin.time.measureTimedValue

data class File(override val id: Int, override val size: Int, override var remaining: Int = size) : Block
data class Gap(override val id: Int, override val size: Int, override var remaining: Int = size, var emptySpace: List<Int> = mutableListOf()) :
    Block

interface Block {
    val id: Int
    val size: Int
    var remaining: Int
}

typealias Input = List<Block>

fun main() {
    val lines = AOC.getInput(9)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input = lines[0].toList()
    .map { it.digitToInt() }
    .withIndex()
    .map { (index, size) ->
        val id = index / 2

        if (index % 2 == 0) {
            File(id, size)
        } else {
            Gap(id, size)
        }
    }

fun part1(input: Input): Long {
    val fs = mutableListOf<Int>()

    input.fold(0) { index, block ->
        fs += (0..<block.size).map { if (block is File) block.id else -1 }
        index + block.size
    }

    var l = 0
    var r = fs.lastIndex

    while (l <= r) {
        // Ignore any gaps at the end
        if (fs[r] == -1) {
            r--
            continue
        }

        // If the current block is a gap, then swap with the block at the end
        if (fs[l] == -1) {
            fs[l] = fs[r].also { fs[r] = fs[l] }
        }

        l++
    }

    return fs.mapIndexed { index, id -> if (id == -1) 0L else (index * id).toLong() }.sum()
}


fun part2(input: Input): Long {
    val blocks = input.fold(listOf<Pair<Block, Int>>() to 0) { list, block ->
        val (files, index) = list

        val newFiles = files + Pair(block, index)
        newFiles to index + block.size
    }.first.toMutableList()

    var l = 0
    var r = blocks.lastIndex

    while (l <= r) {
        // Ignore any gaps at the end
        if (blocks[r].first is Gap) {
            r--
            continue
        }

        // If the current block is a gap, then try to add as many files in reverse order of their IDs
        if (blocks[l].first is Gap && blocks[l].first.remaining > 0) {
            var checkAt = r
            while (l < checkAt) {
                if (blocks[checkAt].first !is Gap) {
                    if (blocks[checkAt].first.size <= blocks[l].first.remaining && blocks[l].second < blocks[checkAt].second) {

                        val fileSize = blocks[checkAt].first.size
                        blocks[l].first.remaining -= fileSize

                        val gapStart = blocks[l].second
                        blocks[checkAt] = blocks[checkAt].first to gapStart
                        blocks[l] = blocks[l].first to gapStart + fileSize
                    }
                }

                checkAt -= 2
            }
        }

        l++
    }

    val files = blocks.filter { it.first is File }

    val checksum = files.sumOf {
        val (file, startIndex) = it
        (startIndex..<(startIndex + file.size)).sumOf { i -> (i * file.id).toLong() }
    }

    return checksum
}
