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

val UP = Position(-1, 0)
val RIGHT = Position(0, 1)
val DOWN = Position(1, 0)
val LEFT = Position(0, -1)
val UP_LEFT = UP + LEFT
val UP_RIGHT = UP + RIGHT
val DOWN_RIGHT = DOWN + RIGHT
val DOWN_LEFT = DOWN + LEFT

val directions = listOf(
    UP_LEFT,
    UP,
    UP_RIGHT,
    RIGHT,
    DOWN_RIGHT,
    DOWN,
    DOWN_LEFT,
    LEFT,
)

fun part1(grid: Grid): Int {
    return grid.indices.sumOf { r ->
        grid[r].indices.sumOf { c ->
            directions.count { dir ->
                foundXMAS(grid, Position(r, c), dir)
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

private fun foundXMAS(grid: Grid, pos: Position, dir: Position): Boolean {
    return "XMAS".withIndex().all { (i, char) -> get(grid, pos + dir * i) == char }
}

private fun foundXshapedMAS(grid: Grid, pos: Position): Boolean {
    val center = get(grid, pos)
    val corners = listOf(
        get(grid, pos + UP_LEFT), // diagonal 1: up left to down right
        get(grid, pos + DOWN_RIGHT),
        get(grid, pos + UP_RIGHT), // diagonal 2: up right to down left
        get(grid, pos + DOWN_LEFT),
    )
    return (center == 'A'
            && corners.count { it == 'M' } == 2
            && corners.count { it == 'S' } == 2
            && corners[0] != corners[1] // check that diagonal is not MAM or SAS
            )
}

private fun get(grid: Grid, pos: Position): Char {
    val (r, c) = pos
    return if (
        r < 0 || c < 0
        || r >= grid.size || c >= grid[0].size
    ) '-'
    else grid[r][c]
}
