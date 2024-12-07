package day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6Test {
    @Test
    fun test() {
        val lines = """
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(41, part1(input))
        assertEquals(6, part2(input))
    }
}