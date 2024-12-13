package day11

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun test() {
        val lines = """
125 17
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(55312, part1(input))
//        assertEquals(1, part2(input))
    }
}