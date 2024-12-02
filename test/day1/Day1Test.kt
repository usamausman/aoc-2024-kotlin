package day1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day1Test {
    @Test
    fun test() {
        val content = """
    3   4
    4   3
    2   5
    1   3
    3   9
    3   3""".trimIndent().lines()
        val lists = parse(content)

        assertEquals(11, part1(lists))
        assertEquals(31, part2(lists))
    }
}