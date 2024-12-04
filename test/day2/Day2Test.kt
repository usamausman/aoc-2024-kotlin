package day2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day2Test {
    @Test
    fun test() {
        val lines = """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
            """.trimIndent().lines()
        val reports = parse(lines)

        assertEquals(2, part1(reports))
        assertEquals(4, part2(reports))
    }
}