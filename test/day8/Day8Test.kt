package day8

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day8Test {
    @Test
    fun test() {
        val lines = """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(14, part1(input))
        assertEquals(34, part2(input))
    }
}