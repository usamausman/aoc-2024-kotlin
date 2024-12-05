package dayN

import utils.AOC
import kotlin.time.measureTimedValue

typealias Input = List<String>

fun main() {
    val lines = AOC.getInput(0)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input = lines


fun part1(input: Input): Int {
    return 1
}

fun part2(input: Input): Int {
    return 1
}
