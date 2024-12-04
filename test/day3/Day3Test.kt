package day3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day3Test {
    @Test
    fun testPart1() {
        val lines = """
xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(161, part1(input))
    }

    @Test
    fun testPart2() {
        val lines = """
xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(48, part2(input))
    }

    @Test
    fun testPart2_needToFindDoAfterDont() {
        val lines = """
mul(1,1)don't()mul(1,1)do()mul(1,1)do()mul(1,1)don't()mul(1,1)
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(3, part2(input))
    }

    @Test
    fun testPart2_subsequentDonts() {
        val lines = """
mul(1,1)don't()mul(1,1)don't()mul(1,1)do()mul(1,1)don't()mul(1,1)
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(2, part2(input))
    }

    @Test
    fun testPart2_worksAcrossLines() {
        val lines = """
mul(1,1)don't()mul(1,1)
mul(1,1)
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(1, part2(input))
    }
}