package day4

import utils.AOC
import kotlin.time.measureTimedValue

typealias Grid = List<List<Char>>

data class Position(val r: Int, val c: Int)

infix operator fun Position.times(n: Int) = Position(r * n, c * n)
infix operator fun Position.plus(d: Position) = Position(r + d.r, c + d.c)

fun main() {
    val lines = AOC.getInput(4)

    val grid = parse(lines)

    val p1 = measureTimedValue { part1(grid) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(grid) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Grid = lines.map { it.toList() }

val directions = listOf(
    Position(1, 0),   // Right
    Position(1, 1),   // Down right
    Position(0, 1),   // Down
    Position(-1, 1),  // Down left
    Position(-1, 0),  // Left
    Position(-1, -1), // Up left
    Position(0, -1),  // Up
    Position(1, -1),  // Up right
)

fun part1(grid: Grid): Int {
    return grid.indices.sumOf { r ->
        grid[r].indices.sumOf { c ->
            directions.count { dir ->
                foundXMAS(grid, r, c, dir)
            }
        }
    }
}

fun part2(grid: Grid): Int {
    return grid.indices.sumOf { r ->
        grid[r].indices.count { c ->
            foundXshapedMAS(grid, Position(r, c))
        }
    }
}


private fun foundXMAS(grid: Grid, r: Int, c: Int, dir: Position): Boolean {
    return "XMAS".withIndex().all { (i, char) -> get(grid, Position(r, c) + dir * i) == char }
}


private fun foundXshapedMAS(grid: Grid, pos: Position): Boolean {
    val center = get(grid, pos)
    val corners = listOf(
        get(grid, pos + Position(-1, -1)),
        get(grid, pos + Position(-1, 1)),
        get(grid, pos + Position(1, -1)),
        get(grid, pos + Position(1, 1)),
    )
    return (center == 'A'
            && corners.count { it == 'M' } == 2
            && corners.count { it == 'S' } == 2
            && corners[0] != corners[3])
}

private fun get(grid: Grid, pos: Position): Char {
    return if (
        pos.r < 0 || pos.c < 0 ||
        pos.r >= grid.size || pos.c >= grid[0].size
    ) '-'
    else grid[pos.r][pos.c]
}



