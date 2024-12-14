package day13

import utils.AOC
import java.math.BigDecimal
import kotlin.time.measureTimedValue

data class BigPosition(val r: BigDecimal, val c: BigDecimal) {
    operator fun plus(other: BigPosition) = BigPosition(r + other.r, c + other.c)
    operator fun minus(other: BigPosition) = BigPosition(r - other.r, c - other.c)
    operator fun times(n: BigDecimal) = BigPosition(r * n, c * n)
}

data class Machine(val prize: BigPosition, val a: BigPosition, val b: BigPosition)
typealias Input = List<Machine>

fun main() {
    val lines = AOC.getInput(13)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input =
    lines.chunked(4)
        .map {
            val aPos = it[0].split(": ")[1].split(", ").map { it.split("+")[1].toBigDecimal() }
            val bPos = it[1].split(": ")[1].split(", ").map { it.split("+")[1].toBigDecimal() }
            val prizePos = it[2].split(": ")[1].split(", ").map { it.split("=")[1].toBigDecimal() }
            val a = BigPosition(aPos[0], aPos[1])
            val b = BigPosition(bPos[0], bPos[1])
            val prize = BigPosition(prizePos[0], prizePos[1])
            Machine(prize, a, b)
        }

fun part1(input: Input): BigDecimal {
    return input.mapNotNull { getButtonPresses(it) }
        .sumOf { it.first.times(BigDecimal(3)) + it.second }
}

fun part2(input: Input): BigDecimal {
    return input.map { fixPrize(it) }
        .mapNotNull { getButtonPresses(it, false) }
        .sumOf { it.first.times(BigDecimal(3)) + it.second }
}

private val tenTrillion = BigPosition(BigDecimal(10_000_000_000_000), BigDecimal(10_000_000_000_000))

private fun fixPrize(machine: Machine): Machine =
    Machine(machine.prize + tenTrillion, machine.a, machine.b)

private fun getButtonPresses(machine: Machine, cappedTo100: Boolean = true): Pair<BigDecimal, BigDecimal>? {
    // From https://en.wikipedia.org/wiki/Cramer%27s_rule#Explicit_formulas_for_small_systems
    // a1 * x + b1 * y = c1
    // a2 * x + b2 * y = c2
    // x = (c1 * b2 - b1 * c2) / (a1 * b2 - b1 * a2)
    // y = (a1 * c2 - c1 * a2) / (a1 * b2 - b1 * a2)

    // In our case
    // a.r * a + b.r * b = prize.r
    // a.c * a + b.c * b = price.c
    // a = (prize.r * b.c - b.r * prize.c) / (a.r * b.c - b.r * a.c)
    // b = (a.r * prize.c - prize.r * a.c) / (a.r * b.c - b.r * a.c)

    val (prize, a, b) = machine
    val denom = a.r * b.c - b.r * a.c
    val aNum = prize.r * b.c - b.r * prize.c
    val bNum = a.r * prize.c - prize.r * a.c

    // No single solution
    if (denom == BigDecimal.ZERO) return null

    val pressA = aNum / denom
    val pressB = bNum / denom

    // If both are integers...
    if (pressA.scale() <= 0 && pressB.scale() <= 0) {
        if (cappedTo100 && (pressA > BigDecimal(100) || pressB > BigDecimal(100))) return null

        val result = a * pressA + b * pressB
        if (result != prize) return null

        return pressA to pressB
    }

    return null
}

