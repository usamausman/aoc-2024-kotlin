package day8

import utils.AOC
import kotlin.math.max
import kotlin.time.measureTimedValue

data class Position(val r: Int, val c: Int) {
    operator fun plus(other: Position) = Position(r + other.r, c + other.c)
    operator fun minus(other: Position) = Position(r - other.r, c - other.c)
    operator fun times(scalar: Int) = Position(r * scalar, c * scalar)
}

data class Grid(val data: List<List<Char>>) {
    fun inBounds(pos: Position): Boolean {
        return pos.r in data.indices && pos.c in data[pos.r].indices
    }

    fun get(pos: Position) {
        data[pos.r][pos.c]
    }
}
typealias Input = Grid

fun main() {
    val lines = AOC.getInput(8)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input = Grid(lines.map { it.toList() })


fun part1(input: Input): Int =
    getAntennas(input).values
        .flatMap { pairs(it).flatMap { pair -> getAntiNodes(input, pair) } }
        .toSet()
        .size

fun part2(input: Input): Int =
    getAntennas(input).values
        .flatMap { pairs(it).flatMap { pair -> getHarmonicNodes(input, pair) } }
        .toSet()
        .size

private fun getAntennas(input: Input): MutableMap<Char, List<Position>> {
    val antennas = mutableMapOf<Char, List<Position>>()
    input.data.indices.forEach { r ->
        input.data[r].indices.forEach { c ->
            val char = input.data[r][c]
            if (char != '.') {
                antennas[char] = antennas.getOrDefault(char, emptyList()) + Position(r, c)
            }
        }
    }
    return antennas
}

private fun getAntiNodes(input: Input, antennaPair: Pair<Position, Position>): List<Position> {
    val (a, b) = antennaPair
    val diff = b - a

    return listOf(a - diff, b + diff)
        .filter { input.inBounds(it) }
}

private fun getHarmonicNodes(input: Input, antennaPair: Pair<Position, Position>): List<Position> {
    val (a, b) = antennaPair
    val diff = b - a

    // Lazy way to calculate all possible harmonic positions
    val backwards = -max(a.r, a.c)
    val forwards = max(input.data.size - a.r, input.data[a.r].size - a.c)

    return (backwards..forwards)
        .map { a + diff * it }
        .filter { input.inBounds(it) }
}

private fun <T> pairs(arr: List<T>): Sequence<Pair<T, T>> = sequence {
    for (i in 0..<arr.size - 1)
        for (j in i + 1..<arr.size)
            yield(arr[i] to arr[j])
}