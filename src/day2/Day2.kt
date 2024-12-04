package day2

import utils.AOC
import kotlin.math.absoluteValue
import kotlin.time.measureTimedValue

typealias Report = List<Int>

fun main() {
    val lines = AOC.getInput(2)

    val reports = parse(lines)

    val p1 = measureTimedValue { part1(reports) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(reports) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>) = lines.map { toReport(it) }

private fun toReport(line: String): Report = line.split("\\s+".toRegex()).map(String::toInt)

fun part1(reports: List<Report>): Int {
    return reports.filter { isSafeNoRemoval(it) }.size
}

fun part2(reports: List<Report>): Int {
    return reports.filter { isSafeWithRemoval(it) }.size
}

private fun isSafeWithRemoval(report: Report): Boolean {
    for (i in report.indices) {
        val removed = report.toMutableList().apply { removeAt(i) }
        if (isSafeNoRemoval(removed)) return true
    }
    return false
}

private fun isSafeNoRemoval(report: Report): Boolean {
    val isIncreasing = report[0] != report[1] && report[0] < report[1]
    for ((prev, curr) in report.windowed(2)) {
        val diff = curr - prev // should be + if isIncreasing, - otherwise
        when {
            isIncreasing && diff < 0 -> return false
            !isIncreasing && diff > 0 -> return false
            diff.absoluteValue !in 1..3 -> return false
        }
    }
    return true
}

