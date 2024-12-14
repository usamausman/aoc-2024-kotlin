package day14

import utils.AOC
import utils.Position
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.time.measureTimedValue

data class Robot(val pos: Position, val vel: Position)
data class Grid(val robots: List<Robot>, val width: Int, val height: Int)
typealias Input = Grid

fun main() {
    val lines = AOC.getInput(14)

    val input = parse(lines, 101, 103)

    val p1 = measureTimedValue { part1(input) }
    println("Part 1 (${p1.duration}): ${p1.value}")

    val p2 = measureTimedValue { part2(input) }
    println("Part 2 (${p2.duration}): ${p2.value}")
}

private val REGEX_ROBOT = Regex("p=(.*),(.*) v=(.*),(.*)")

fun parse(lines: List<String>, width: Int, height: Int): Input =
    Grid(
        lines.map { line ->
            val (px, py, vx, vy) = REGEX_ROBOT.find(line)!!.groupValues.drop(1).map { it.toInt() }
            Robot(Position(py, px), Position(vy, vx))
        },
        width,
        height
    )

fun part1(input: Input): Int {
    val map = input.robots.map { wrap(it.pos, it.vel * 100, input.width, input.height) }

    return map
        .groupingBy { toQuadrant(it, input.width, input.height) }
        .eachCount()
        .filter { it.key != null }
        .map { it.value }
        .fold(1) { acc, count -> acc * count }
}

fun part2(input: Input): Int {
    var i = 0

    while (true) {
        val map = input.robots.map { wrap(it.pos, it.vel * i, input.width, input.height) }
        if (hasNeighbours(map)) {
            saveImage(map, input.width, input.height, i)
            return i
        }
        i++
    }
}

private fun hasNeighbours(robots: List<Position>): Boolean {
    val pos = robots.toSet()
    // Check 3x3 around each position
    pos.forEach { p ->
        val neighbors = (-1..1).flatMap { r -> (-1..1).map { c -> p + Position(r, c) } }
        if (neighbors.all { pos.contains(it) }) return true
    }
    return false
}

private fun saveImage(pos: List<Position>, width: Int, height: Int, step: Int) {
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    pos.forEach {
        image.setRGB(it.c, it.r, 0xFFFFFF)
    }
    val file = File("day14/$step.bmp")
    ImageIO.write(image, "bmp", file)
}

private fun toQuadrant(p: Position, width: Int, height: Int): Int? {
    val x = p.c
    val y = p.r
    val midX = width / 2
    val midY = height / 2

    if (x == midX || y == midY) return null
    return if (x < midX) {
        if (y < midY) 1 else 2
    } else {
        if (y < midY) 3 else 4
    }
}

private fun wrap(start: Position, vel: Position, width: Int, height: Int): Position {
    val x = (start.c + vel.c).mod(width)
    val y = (start.r + vel.r).mod(height)
    return Position(y, x)
}
