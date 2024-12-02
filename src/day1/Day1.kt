package day1

import utils.AOC
import kotlin.math.absoluteValue
import kotlin.time.measureTimedValue


fun main() {
    val lines = AOC.getInput(1)

    val lists = parse(lines)

    val p1 = measureTimedValue { part1(lists) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(lists) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Pair<List<Int>, List<Int>> {
    val numbers = lines.map {
        val numbers = it.split("\\s+".toRegex()).map(String::toInt)
        Pair(numbers[0], numbers[1])
    }.unzip()
    return Pair(numbers.first.sorted(), numbers.second.sorted())
}

fun part1(lists: Pair<List<Int>, List<Int>>): Int {
    val (left, right) = lists
    return left.zip(right).sumOf { (l, r) -> (l - r).absoluteValue }
}

fun part2(lists: Pair<List<Int>, List<Int>>): Int {
    val (left, right) = lists
    val rightCounts = right.groupBy { it }.mapValues { it.value.size }
    return left.sumOf { l -> l * (rightCounts[l] ?: 0) }
}
