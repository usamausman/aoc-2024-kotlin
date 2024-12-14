package utils

import java.io.File

class AOC {
    companion object {
        fun getInput(day: Int): List<String> {
            return File("./input/day_$day.txt").readLines()
        }
    }
}

data class Position(val r: Int, val c: Int) {
    operator fun plus(dir: Direction) = Position(r + dir.r, c + dir.c)
    operator fun plus(pos: Position) = Position(r + pos.r, c + pos.c)
    operator fun times(n: Int) = Position(r * n, c * n)
}

enum class Direction(val r: Int, val c: Int) {
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1),
    UP(-1, 0);
}

data class Grid<T>(val data: List<List<T>>, val width: Int, val height: Int) {
    operator fun get(pos: Position): T? =
        if (pos.r < 0 || pos.r >= height || pos.c < 0 || pos.c >= width) null
        else data[pos.r][pos.c]

    fun positions(): Sequence<Position> = sequence {
        for (r in 0..<height) {
            for (c in 0..<width) {
                yield(Position(r, c))
            }
        }
    }
}
