import java.lang.Integer.min
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    fun List<String>.toLines(): List<Line> = this.map { line ->
        val points = line.split(" -> ").map {
            val values = it.split(",")
            Point(x = values[0].toInt(), y = values[1].toInt())
        }
        Line(points[0], points[1])
    }

    fun processInput(input: List<String>): Pair<List<Line>, Array<IntArray>> {
        val inputLines = input.toLines()
        val xDim = inputLines.maxOf { line -> max(line.first.x, line.second.x) }
        val yDim = inputLines.maxOf { line -> max(line.first.y, line.second.y) }
        val oceanFloor = Array(yDim + 1) { IntArray(xDim + 1) }
        return Pair(inputLines, oceanFloor)
    }

    fun part1(input: List<String>): Int {
        val (inputLines, oceanFloor) = processInput(input)
        for ((from, to) in inputLines) {
            if (from.x == to.x) {
                val start = min(from.y, to.y)
                val length = (from.y - to.y).absoluteValue
                for (i in 0..length) oceanFloor[start + i][from.x]++
            } else if (from.y == to.y) {
                val start = min(from.x, to.x)
                val length = (from.x - to.x).absoluteValue
                for (i in 0..length) oceanFloor[from.y][start + i]++
            }
        }
        return oceanFloor.sumOf { line -> line.count { it > 1 } }
    }

    fun part2(input: List<String>): Int {
        val (inputLines, oceanFloor) = processInput(input)
        for ((from, to) in inputLines) {
            if (from.x == to.x) {
                val start = min(from.y, to.y)
                val length = (from.y - to.y).absoluteValue
                for (i in 0..length) oceanFloor[start + i][from.x]++
            } else if (from.y == to.y) {
                val start = min(from.x, to.x)
                val length = (from.x - to.x).absoluteValue
                for (i in 0..length) oceanFloor[from.y][start + i]++
            } else if (from.x - from.y == to.x - to.y) {
                val startX = min(from.x, to.x)
                val startY = min(from.y, to.y)
                val length = (from.x - to.x).absoluteValue
                for (i in 0..length) oceanFloor[startY + i][startX + i]++
            } else if (from.x - to.x == to.y - from.y) {
                val startX = min(from.x, to.x)
                val startY = max(from.y, to.y)
                val length = (from.x - to.x).absoluteValue
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


typealias Line = Pair<Point, Point>

data class Point(val x: Int, val y: Int)
