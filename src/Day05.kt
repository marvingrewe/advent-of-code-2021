import java.lang.Integer.min
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    fun List<String>.toLines(): List<Line> = this.map { line ->
        val points = line.split(" -> ").map {
            val values = it.split(",")
            Point(first = values[0].toInt(), second = values[1].toInt())
        }
        Line(points[0], points[1])
    }

    fun processInput(input: List<String>): Pair<List<Line>, Array<IntArray>> {
        val inputLines = input.toLines()
        val xDim = inputLines.maxOf { line -> max(line.first.first, line.second.first) }
        val yDim = inputLines.maxOf { line -> max(line.first.second, line.second.second) }
        val oceanFloor = Array(yDim + 1) { IntArray(xDim + 1) }
        return Pair(inputLines, oceanFloor)
    }

    fun part1(input: List<String>): Int {
        val (inputLines, oceanFloor) = processInput(input)
        for ((from, to) in inputLines) {
            if (from.first == to.first) {
                val start = min(from.second, to.second)
                val length = (from.second - to.second).absoluteValue
                for (i in 0..length) oceanFloor[start + i][from.first]++
            } else if (from.second == to.second) {
                val start = min(from.first, to.first)
                val length = (from.first - to.first).absoluteValue
                for (i in 0..length) oceanFloor[from.second][start + i]++
            }
        }
        return oceanFloor.sumOf { line -> line.count { it > 1 } }
    }

    fun part2(input: List<String>): Int {
        val (inputLines, oceanFloor) = processInput(input)
        for ((from, to) in inputLines) {
            if (from.first == to.first) {
                val start = min(from.second, to.second)
                val length = (from.second - to.second).absoluteValue
                for (i in 0..length) oceanFloor[start + i][from.first]++
            } else if (from.second == to.second) {
                val start = min(from.first, to.first)
                val length = (from.first - to.first).absoluteValue
                for (i in 0..length) oceanFloor[from.second][start + i]++
            } else if (from.first - from.second == to.first - to.second) {
                val startX = min(from.first, to.first)
                val startY = min(from.second, to.second)
                val length = (from.first - to.first).absoluteValue
                for (i in 0..length) oceanFloor[startY + i][startX + i]++
            } else if (from.first - to.first == to.second - from.second) {
                val startX = min(from.first, to.first)
                val startY = max(from.second, to.second)
                val length = (from.first - to.first).absoluteValue
                for (i in 0..length) oceanFloor[startY - i][startX + i]++
            }
        }
        return oceanFloor.sumOf { line -> line.count { it > 1 } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")

    println(part1(input))
    println(part2(input))

    val speedPart1 = measureTimeMillis { repeat(1000) { part1(input) } }
    val speedPart2 = measureTimeMillis { repeat(1000) { part2(input) } }
    println("part1: ${speedPart1}ms")
    println("part2: ${speedPart2}ms")
}
