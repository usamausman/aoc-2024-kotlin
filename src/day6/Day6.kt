package day6

import utils.AOC
import kotlin.time.measureTimedValue

data class Input(val grid: List<List<Char>>) {
    fun findGuard(): Guard {
        grid.indices.forEach { r ->
            grid[r].indices.forEach { c ->
                val pos = Position(r, c)
                if (get(pos) == '^') return Guard(pos, Direction.UP)
            }
        }
        return Guard(Position(-1, -1), Direction.RIGHT)
    }


    fun get(pos: Position) =
        grid[pos.r][pos.c]

    fun contains(pos: Position) = pos.r in grid.indices && pos.c in grid[0].indices
}

data class Position(val r: Int, val c: Int) {
    operator fun plus(dir: Direction) =
        Position(r + dir.r, c + dir.c)
}

data class Guard(val pos: Position, val dir: Direction)

enum class Direction(val r: Int, val c: Int) {
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1),
    UP(-1, 0);

    fun right() = when (this) {
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
        UP -> RIGHT
    }
}


fun main() {
    val lines = AOC.getInput(6)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input = Input(lines.map { it.toList() })


fun part1(input: Input): Int {
    var guard = input.findGuard()
    val visited = mutableSetOf<Position>()

    while (input.contains(guard.pos)) {
        visited.add(guard.pos)

        val nextPos = guard.pos + guard.dir
        if (!input.contains(nextPos)) break

        val nextIsObstacle = input.get(nextPos) == '#'

        guard =
            if (nextIsObstacle) Guard(guard.pos, guard.dir.right())
            else Guard(nextPos, guard.dir)
    }

    return visited.size
}

fun part2(input: Input): Int {
    val initialGuard = input.findGuard()
    var guard = initialGuard
    val visited = mutableSetOf<Position>()

    val possibleObstacles = mutableSetOf<Position>()

    while (input.contains(guard.pos)) {
        visited.add(guard.pos)

        val nextPos = guard.pos + guard.dir
        if (!input.contains(nextPos)) break

        val isObstacle = input.get(nextPos) == '#'
        if (!isObstacle && nextPos !in visited && wouldLoopWith(input, guard, nextPos)) {
            possibleObstacles.add(nextPos)
        }

        guard =
            if (isObstacle) Guard(guard.pos, guard.dir.right())
            else Guard(nextPos, guard.dir)
    }

    return possibleObstacles.size
}

fun wouldLoopWith(input: Input, initialGuard: Guard, newObstacle: Position): Boolean {
    var guard = initialGuard
    val pastGuards = mutableSetOf<Guard>()

    while (input.contains(guard.pos)) {
        pastGuards.add(guard)

        val nextPos = guard.pos + guard.dir
        if (!input.contains(nextPos)) break

        val isObstacle = nextPos == newObstacle || input.get(nextPos) == '#'

        guard =
            if (isObstacle) Guard(guard.pos, guard.dir.right())
            else Guard(nextPos, guard.dir)

        if (guard in pastGuards) return true
    }

    return false
}
