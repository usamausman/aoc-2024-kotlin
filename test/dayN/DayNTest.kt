package dayN

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DayNTest {
    @Test
    fun test() {
        val lines = """
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(1, part1(input))
        assertEquals(1, part2(input))
    }
}