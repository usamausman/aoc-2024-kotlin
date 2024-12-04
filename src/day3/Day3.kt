package day3

import utils.AOC
import kotlin.time.measureTimedValue

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
    return followInstructions(line).sum()
}

fun part2(line: String): Int {
    return followEnabledInstructions(line).sum()
}


private fun followInstructions(line: String): List<Int> {
    return """mul\((\d+),(\d+)\)""".toRegex().findAll(line)
        .map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
        .toList()
}

private fun followEnabledInstructions(line: String): List<Int> {
    return line.split("don't()") // Split on `don't()`
        .mapIndexed { index, it ->
            // First match is enabled by default, later matches need to find `do()`
            if (index == 0) it
            else it.substringAfter("do()", missingDelimiterValue = "") // Return empty string if `do()` is not found
        }
        .flatMap { followInstructions(it) }
        .toList()
}

