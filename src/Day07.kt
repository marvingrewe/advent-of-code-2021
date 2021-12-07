import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val crabs = input[0].split(",").map(String::toInt)
        val maxIndex = crabs.maxOf { it }
        var oil = Int.MAX_VALUE
        for (i in 0..maxIndex) {
            val currentOil = crabs.sumOf { (it - i).absoluteValue }
            if (currentOil < oil) {
                oil = currentOil
            }
        }
        //println("Position: $position, Oil: $oil")
        return oil
    }

    fun part2(input: List<String>): Int {
        val crabs = input[0].split(",").map(String::toInt)
        val maxIndex = crabs.maxOf { it }
        var oil = Int.MAX_VALUE
        for (i in 0..maxIndex) {
            val currentOil = crabs.sumOf { (it - i).absoluteValue * ((it - i).absoluteValue + 1) / 2}
            if (currentOil < oil) {
                oil = currentOil
            }
        }
        //println("Position: $position, Oil: $oil")
        return oil
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    var result: Any

    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}
