package day7

import utils.AOC
import kotlin.time.measureTimedValue

data class RevOperation(val canReverse: (Long, Long) -> Boolean, val reverse: (Long, Long) -> Long)
data class Equation(val total: Long, val numbers: List<Long>) {
    fun possibleWith(operations: List<RevOperation>): Boolean {
        return recursiveCheck(operations, total, numbers.lastIndex)
    }

    private fun recursiveCheck(operations: List<RevOperation>, acc: Long, lastIndex: Int): Boolean {
        if (lastIndex == -1) return acc == 0L

        val right = numbers[lastIndex]

        return operations
            .filter { it.canReverse(acc, right) }
            .map { it.reverse(acc, right) }
            .any { recursiveCheck(operations, it, lastIndex - 1) }
    }
}

typealias Input = List<Equation>

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

val ADD = RevOperation(
    { a, b -> a - b >= 0 },
    { a, b -> a - b }
)
val MUL = RevOperation(
    { a, b -> a % b == 0L },
    { a, b -> a / b }
)
val CONCAT = RevOperation(
    { a, b -> a.toString().endsWith(b.toString()) },
    { a, b -> if (a != b) a.toString().removeSuffix(b.toString()).toLong() else 0 }
)

fun part1(input: Input): Long =
    input
        .filter { it.possibleWith(listOf(ADD, MUL)) }
        .sumOf { it.total }

fun part2(input: Input): Long =
    input
        .filter { it.possibleWith(listOf(ADD, MUL, CONCAT)) }
        .sumOf { it.total }
