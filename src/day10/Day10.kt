package day10

import utils.AOC
import kotlin.time.measureTimedValue

data class Position(val r: Int, val c: Int) {
    operator fun plus(dir: Direction) = Position(r + dir.r, c + dir.c)
}

enum class Direction(val r: Int, val c: Int) {
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1),
    UP(-1, 0);
}

data class Grid(val data: List<List<Int>>, val width: Int, val height: Int) {
    operator fun get(pos: Position): Int? =
        if (pos.r < 0 || pos.r >= height || pos.c < 0 || pos.c >= width) null
        else data[pos.r][pos.c]

    fun positions(): Sequence<Position> = sequence {
        for (r in 0..<height) {
            for (c in 0..<width) {
                yield(Position(r, c))
            }
        }
    }
}

typealias Input = Grid

fun main() {
    val lines = AOC.getInput(10)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input =
    lines.map { it.toList().map { c -> if (c.isDigit()) c.digitToInt() else -1 } }.let { Grid(it, it.size, it[0].size) }

fun part1(input: Input): Int {
    // We are working backwards from the tops
    val trails = getTops(input)

    // Until we get to 0...
    repeat(9) {
        trails.forEach { (top, ends) ->
            trails[top] = ends.flatMap { end ->
                findValidSteps(input, end)
            }.toSet()
        }
    }

    return trails.values.sumOf { tops -> tops.size }
}

fun part2(input: Input): Int {
    // Accumulate the ratings
    val ratings = input.positions().map { it to 0 }.toMap().toMutableMap()
    // Keep a record of edges we've seen
    val edgesVisited = mutableSetOf<Pair<Position, Position>>()

    // We want to work backwards so that we are calculating the rating on the way from 9 to 0
    val trails = getTops(input)

    trails.forEach { (top, _) -> ratings[top] = 1 }

    repeat(9) {
        trails.forEach { (top, ends) ->
            trails[top] = ends.flatMap { end ->
                findValidSteps(input, end)
                    .onEach {
                        // If we haven't seen an edge before, update the rating for the destination
                        val edge = it to end
                        if (edge !in edgesVisited) {
                            edgesVisited.add(edge)
                            ratings[it] = ratings[it]!! + ratings[end]!!
                        }
                    }
            }.toSet()
        }
    }

    return input.positions().filter { input[it] == 0 }
        .sumOf { ratings[it]!! }
}

// Return all possible next positions
private fun findValidSteps(input: Input, end: Position): List<Position> {
    val currentHeight = input[end]!!
    return Direction.entries
        .map { end + it }
        .filter { input[it] == currentHeight - 1 }
}

// Get the tops and a list of locations to visit
private fun getTops(input: Input): MutableMap<Position, Set<Position>> {
    return input.positions()
        .filter { input[it] == 9 }
        .map { it to setOf(it) }
        .toMap().toMutableMap()
}
