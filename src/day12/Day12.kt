package day12

import utils.AOC
import utils.Direction
import utils.Grid
import utils.Position
import kotlin.time.measureTimedValue

typealias Input = Grid<Char>

data class Plot(val positions: Set<Position>) {
    fun area(): Int = positions.size
    fun perimeter(): Int =
        positions.sumOf { pos ->
            val edges = Direction.entries
                .filter { pos + it !in positions }
            edges.size
        }

    fun sides(): Int {
        // 2x2 grids which contain a cell
        val squares = listOf(
            Direction.LEFT to Direction.UP,
            Direction.UP to Direction.RIGHT,
            Direction.RIGHT to Direction.DOWN,
            Direction.DOWN to Direction.LEFT,
        )
        // Corners which look like
        // x is not considered
        // .a
        // x.
        val outerCorners = positions.sumOf { pos ->
            squares.filter { (a, b) ->
                pos + a !in positions && pos + b !in positions
            }.size
        }
        // Corners which look like
        // aa
        // .a
        val innerCorners = positions.sumOf { pos ->
            squares.filter { (a, b) ->
                pos + a in positions && pos + b in positions && pos + a + b !in positions
            }.size
        }

        return outerCorners + innerCorners
    }
}

fun main() {
    val lines = AOC.getInput(12)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input = lines.map { it.toList() }.let {
    Grid(it, it.size, it[0].size)
}

fun part1(input: Input): Int {
    val plots = getPlots(input)

    return plots.sumOf { it.area() * it.perimeter() }
}

fun part2(input: Input): Int {
    val plots = getPlots(input)

    return plots.sumOf { it.area() * it.sides() }
}

private fun getPlots(input: Input): MutableList<Plot> {
    val allPos = input.positions().toMutableSet()
    val plots = mutableListOf<Plot>()

    while (allPos.isNotEmpty()) {
        val p = allPos.first()
        val c = input[p]!!

        val plotSquares = mutableSetOf(p)
        val neighbors = possibleNeighbors(p, plotSquares, input, c).toSet().toMutableSet()

        while (neighbors.isNotEmpty()) {
            val n = neighbors.first()
            if (input[n] != null) {
                plotSquares.add(n)
                neighbors.addAll(possibleNeighbors(n, plotSquares, input, c))
            }
            neighbors.remove(n)
        }

        allPos.removeAll(plotSquares)
        plots.add(Plot(plotSquares))
    }
    return plots
}

private fun possibleNeighbors(
    pos: Position,
    plotSquares: MutableSet<Position>,
    grid: Input,
    c: Char
) = Direction.entries.map { pos + it }
    .filter { it !in plotSquares && grid[it] == c }
