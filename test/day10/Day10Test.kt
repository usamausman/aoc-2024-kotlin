package day10

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun test() {
        val lines = """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(36, part1(input))
        assertEquals(81, part2(input))
    }

    @Test
    fun testSimple() {
        val lines = """
0123
1234
8765
9876
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(1, part1(input))
        assertEquals(16, part2(input))
    }

    @Test
    fun testSplit() {
        val lines = """
...0...
...1...
...2...
6543456
7.....7
8.....8
9.....9            
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(2, part1(input))
        assertEquals(2, part2(input))
    }

    @Test
    fun testMultiple() {
        val lines = """
.....0.
..4321.
..5..2.
..6543.
..7..4.
..8765.
..9....            
            """.trimIndent().lines()
        val input = parse(lines)

        assertEquals(1, part1(input))
        assertEquals(3, part2(input))
    }
}