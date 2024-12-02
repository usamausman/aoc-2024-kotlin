package utils

import java.io.File

class AOC {
    companion object {
        fun getInput(day: Int): List<String> {
            return File("./input/day_$day.txt").readLines()
        }
    }
}
