package day3

import utils.AOC
import kotlin.time.measureTimedValue

typealias Instruction = Pair<Int, Int>

fun main() {
    val lines = AOC.getInput(3)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>) = lines.joinToString("")

fun part1(line: String): Int {
    return findInstructions(line).sumOf { (a, b) -> a * b }
}

fun part2(line: String): Int {
    return findEnabledInstructions(line).sumOf { (a, b) -> a * b }
}


private fun findInstructions(line: String): List<Instruction> {
    return """mul\((\d+),(\d+)\)""".toRegex().findAll(line)
        .map { Instruction(it.groupValues[1].toInt(), it.groupValues[2].toInt()) }
        .toList()
}

private fun findEnabledInstructions(line: String): List<Instruction> {
    return line.split("don't()") // Split on `don't()`
        .mapIndexed { index, it ->
            // First match is enabled by default, later matches need to find `do()`
            if (index == 0) it
            else it.substringAfter("do()", missingDelimiterValue = "") // Return empty string if `do()` is not found
        }
        .flatMap { findInstructions(it) }
        .toList()
}

