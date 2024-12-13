package day11

import utils.AOC
import java.math.BigInteger
import kotlin.time.measureTimedValue

typealias Input = List<BigInteger>

fun main() {
    val lines = AOC.getInput(11)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input = lines[0].split(" ").map { it.toBigInteger() }


fun part1(input: Input): BigInteger {
    return blinkMultiple(input, 25)
}

fun part2(input: Input): BigInteger {
    return blinkMultiple(input, 75)
}

private fun blinkMultiple(initial: List<BigInteger>, blinks: Int): BigInteger {
    val next: MutableMap<BigInteger, BigInteger> = initial.sorted().groupingBy { it }.eachCount()
        .mapValuesTo(mutableMapOf()) { (_, count) -> count.toBigInteger() }

    repeat(blinks) {
        val current = next.toMutableMap()
        next.clear()

        current.forEach { (stone, count) ->
            if (stone == BigInteger.ZERO) {
                addCount(next, BigInteger.ONE, count)
            } else if (stone.toString().length % 2 == 0) {
                val (left, right) = stone.toString().chunked(stone.toString().length / 2).map { chunk -> chunk.toBigInteger() }

                addCount(next, left, count)
                addCount(next, right, count)
            } else {
                addCount(next, stone.times(BigInteger.valueOf(2024)), count)
            }
        }
    }

    return next.entries.sumOf { (_, count) -> count }
}

private fun addCount(next: MutableMap<BigInteger, BigInteger>, stone: BigInteger, count: BigInteger) {
    next[stone] = (next[stone] ?: BigInteger.ZERO).plus(count)
}
