package day9

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day9Test {
    @Test
    fun test() {
        val lines = """
2333133121414131402
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(1928, part1(input))
        assertEquals(2858, part2(input))
    }

    @Test
    fun emptyFiles() {
        val lines = """
02101
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(1, part1(input))
    }

    @Test
    fun extraGapAtEnd() {
        val lines = """
021012020
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(1, part1(input))
    }
}