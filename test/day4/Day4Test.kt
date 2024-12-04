package day4

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day4Test {
    @Test
    fun test() {
        val lines = """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(18, part1(input))
        assertEquals(9, part2(input))
    }
}