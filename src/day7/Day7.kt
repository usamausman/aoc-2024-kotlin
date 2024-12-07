package day7

import utils.AOC
import kotlin.time.measureTimedValue

typealias Operation = (Long, Long) -> Long
typealias Input = List<Equation>

data class Equation(val total: Long, val numbers: List<Long>) {
    fun possibleWith(operations: List<Operation>): Boolean {
        return recursiveCheck(operations, numbers[0], 1)
    }

    private fun recursiveCheck(operations: List<Operation>, acc: Long, startIndex: Int): Boolean {
        if (startIndex == numbers.size) return total == acc

        val right = numbers[startIndex]

        return operations
            .map { it(acc, right) }
            .filter { it <= total }
            .any { recursiveCheck(operations, it, startIndex + 1) }
    }
}

fun main() {
    val lines = AOC.getInput(7)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input = lines.map { line ->
    val (total, numbers) = line.split(":", limit = 2).map { it.trim() }
    Equation(total.toLong(), numbers.split(" ").map { it.toLong() })
}

private fun add(a: Long, b: Long) = a + b
private fun mul(a: Long, b: Long) = a * b
private fun concat(a: Long, b: Long) = "$a$b".toLong()

fun part1(input: Input): Long =
    input
        .filter { it.possibleWith(listOf(::add, ::mul)) }
        .sumOf { it.total }

fun part2(input: Input): Long =
    input
        .filter { it.possibleWith(listOf(::add, ::mul, ::concat)) }
        .sumOf { it.total }
