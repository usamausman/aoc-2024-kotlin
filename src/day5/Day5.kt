package day5

import utils.AOC
import kotlin.time.measureTimedValue

data class Rule(val left: Int, val right: Int)
data class Pages(val pageIndices: Map<Int, Int>, val middle: Int, val indexOfMiddle: Int)

typealias Input = Pair<List<Rule>, List<Pages>>

fun main() {
    val lines = AOC.getInput(5)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input {
    val emptyLine = lines.indexOf("")

    val rules = lines.subList(0, emptyLine).map { r ->
        val (left, right) = r.split("|", limit = 2).map { it.toInt() }
        Rule(left, right)
    }
    val pages = lines.subList(emptyLine + 1, lines.size).map { r ->
        val pages = r.split(",").map { it.toInt() }
        val pageIndices = pages.withIndex().associate { (i, n) -> n to i }

        Pages(pageIndices, pages[pages.size / 2], pages.size / 2)
    }
    return Pair(rules, pages)
}

fun part1(input: Input): Int {
    val (rules, pageOrders) = input
    return pageOrders.filter { allPass(it, rules) }
        .sumOf { it.middle }
}

fun part2(input: Input): Int {
    val (rules, pageOrders) = input
    return pageOrders.filter { !allPass(it, rules) }
        .map { fix(it, rules.filter { rule -> rule.applies(it) }) }
        .sumOf { it.middle }
}

fun fix(pages: Pages, rules: List<Rule>): Pages {
    // Iterate until all rules pass
    if (allPass(pages, rules)) return pages
    else {
        // Apply all rules
        val newPages = rules.fold(pages) { acc, rule ->
            if (!rule.passes(acc)) {
                // Swap indices
                val newPages = acc.pageIndices.toMutableMap()
                newPages[rule.left] = acc.pageIndices[rule.right]!!
                newPages[rule.right] = acc.pageIndices[rule.left]!!
                // Update middle if it has changed
                val newMiddle = when (acc.indexOfMiddle) {
                    newPages[rule.left] -> rule.left
                    newPages[rule.right] -> rule.right
                    else -> acc.middle
                }

                Pages(newPages, newMiddle, acc.indexOfMiddle)
            } else acc
        }
        return fix(newPages, rules)
    }
}

private fun allPass(pages: Pages, rules: List<Rule>) =
    rules.filter { it.applies(pages) }
        .all { it.passes(pages) }

private fun Rule.applies(page: Pages) = this.left in page.pageIndices && this.right in page.pageIndices
private fun Rule.passes(p: Pages) = p.pageIndices[this.left]!! < p.pageIndices[this.right]!!
