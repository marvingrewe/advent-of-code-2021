import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        val regex = "-?\\d+".toRegex()
        val matches = regex.findAll(input.first())
        val ints = matches.map { it.value.toInt() }.toList()
        val xRange = ints[0]..ints[1]
        val yRange = ints[2]..ints[3]
        val xMin = highestTriangularBelow(xRange.first) // minimum velocity required to reach lower x bound
        val xMax = xRange.last // maximum velocity to still hit upper x bound
        val yMin = yRange.first // minimum velocity to still hit lower y bound
        val yMax = abs(yRange.first) // maximum velocity to still hit lower y bound
        var validVelocities = 0
        var iterations = 0

        for (x in xMin..xMax) {
            for (y in yMin..yMax) {
                var xPos = 0
                var yPos = 0
                var xVel = x
                var yVel = y
                while (xPos <= xRange.last && yPos >= yRange.first) {
                    iterations++
                    xPos += xVel
                    yPos += yVel
                    if (xVel > 0) xVel-- else if (xVel < 0) xVel++
                    yVel--
                    if (xPos in xRange && yPos in yRange) {
                        validVelocities++
                        break
                    }
                }
            }
        }
        println(iterations)
        return validVelocities
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")

    val input = readInput("Day17")
    var result: Any

    println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result: $result, expected: ")
    println("Test 2 solved in ${measureTimeMillis { result = part2(testInput) }}ms with result: $result, expected: 112")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}
