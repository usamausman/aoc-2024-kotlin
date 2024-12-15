package day15

import utils.AOC
import utils.Direction
import utils.Grid
import utils.Position
import kotlin.time.measureTimedValue

data class Move(val direction: Direction) {
    companion object {
        fun of(c: Char) = when (c) {
            '^' -> Move(Direction.UP)
            '>' -> Move(Direction.RIGHT)
            'v' -> Move(Direction.DOWN)
            '<' -> Move(Direction.LEFT)
            else -> throw IllegalArgumentException("Invalid direction: $c")
        }
    }
}

typealias WMap = Map<Position, Char>

data class Input(val map: WMap, val robot: Position, val moves: List<Move>)

fun main() {
    val lines = AOC.getInput(15)

    val input = parse(lines)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

fun parse(lines: List<String>): Input {
    val gridLines = lines.takeWhile { it.isNotEmpty() }.map { it.toList() }
    val grid = Grid(gridLines, gridLines[0].size, gridLines.size)

    val robot = grid.positions().filter { grid[it] == '@' }.first()

    val map = grid.positions().map { it to grid[it]!! }
        .filter { (_, cell) -> cell == '#' || cell == 'O' }
        .toMap()

    val moves = lines.drop(gridLines.size + 1).joinToString("").toList().map { Move.of(it) }
    return Input(map, robot, moves)
}


fun part1(input: Input): Int {
    var (map, robot, moves) = input
    map = map.toMutableMap()

    moves.forEach { move ->
        val (isBlocked, boxes) = findBoxes(map, robot, move)
        if (isBlocked) return@forEach

        boxes.onEach { box -> map.remove(box) }
            .onEach { box -> map[box + move.direction] = 'O' }
        robot += move.direction

    }

    // show(map, robot)
    return map.filter { (_, cell) -> cell == 'O' }
        .keys
        .sumOf { 100 * it.r + it.c }
}

fun part2(input: Input): Int {
    val moves = input.moves
    var (map, robot) = makeWide(input.map, input.robot)
    map = map.toMutableMap()

    moves.forEach { move ->
        val (isBlocked, boxes) = findWideBoxes(map, robot, move)
        if (isBlocked) return@forEach

        val boxesWithCell = boxes.map { it to map[it]!! }

        boxesWithCell.onEach { (pos, _) -> map.remove(pos) }
            .onEach { (pos, cell) -> map[pos + move.direction] = cell }
        robot += move.direction
    }

    // show(map, robot)
    return map.filter { (_, cell) -> cell == '[' }
        .keys
        .sumOf { 100 * it.r + it.c }
}

private fun makeWide(map: WMap, robot: Position): Pair<WMap, Position> {
    val newMap = mutableMapOf<Position, Char>()
    val newRobot = Position(robot.r, robot.c * 2)

    map.forEach { (pos, cell) ->
        when (cell) {
            '#' -> {
                newMap[Position(pos.r, pos.c * 2)] = '#'
                newMap[Position(pos.r, pos.c * 2 + 1)] = '#'
            }

            'O' -> {
                newMap[Position(pos.r, pos.c * 2)] = '['
                newMap[Position(pos.r, pos.c * 2 + 1)] = ']'
            }

            else -> throw IllegalArgumentException("Invalid cell: $cell")
        }
    }

    return newMap to newRobot
}

private fun findWideBoxes(map: WMap, robot: Position, move: Move): Pair<Boolean, List<Position>> {
    val next = mutableSetOf(robot + move.direction)
    val boxes = mutableListOf<Position>()

    while (next.isNotEmpty()) {
        val check = next.first()

        // Blocked
        if (map[check] == '#') {
            return true to listOf()
        } else if (map[check] == '[' || map[check] == ']') {
            val (left, right) =
                if (map[check] == '[') check to check + Direction.RIGHT
                else check + Direction.LEFT to check

            boxes.add(left)
            boxes.add(right)

            next.add(left + move.direction)
            next.add(right + move.direction)
        }

        next.remove(check)
    }
    return false to boxes
}

private fun findBoxes(map: WMap, robot: Position, move: Move): Pair<Boolean, List<Position>> {
    val boxes = mutableListOf<Position>()

    var next = robot + move.direction
    while (map[next] == 'O') {
        boxes.add(next)
        next += move.direction
    }

    // Blocked
    if (map[next] == '#') return true to listOf()
    return false to boxes
}

private fun show(map: WMap, robot: Position) {
    val maxC = map.keys.maxOf { it.c }
    val maxR = map.keys.maxOf { it.r }

    for (r in 0..maxR) {
        for (c in 0..maxC) {
            val pos = Position(r, c)
            if (pos == robot) {
                print('@')
            } else {
                print(map[pos] ?: ' ')
            }
        }
        println()
    }
}

