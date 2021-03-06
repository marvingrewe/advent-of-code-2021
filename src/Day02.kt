import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int = input.fold(Pair(0, 0)) { (depth, distance), element ->
        val (direction, value) = element.split(" ")
        when (direction) {
            "forward" -> Pair(depth, distance + value.toInt())
            "down" -> Pair(depth + value.toInt(), distance)
            "up" -> Pair(depth - value.toInt(), distance)
            else -> throw IllegalArgumentException()
        }
    }.let { it.first * it.second }

    fun part2(input: List<String>): Int = input.fold(Triple(0, 0, 0)) { (depth, distance, aim), element ->
        val (direction, value) = element.split(" ")
        when (direction) {
            "forward" -> Triple(depth + aim * value.toInt(), distance + value.toInt(), aim)
            "down" -> Triple(depth, distance, aim + value.toInt())
            "up" -> Triple(depth, distance, aim - value.toInt())
            else -> throw IllegalArgumentException()
        }
    }.let { it.first * it.second }

    fun part2func(input: List<String>): Int =
        input.map { element -> element.split(" ").let { Pair(it[0], it[1].toInt()) } }
            .fold(Triple(0, 0, 0)) { (depth, distance, aim), (direction, value) ->
                when (direction) {
                    "forward" -> Triple(depth + aim * value, distance + value, aim)
                    "down" -> Triple(depth, distance, aim + value)
                    "up" -> Triple(depth, distance, aim - value)
                    else -> throw IllegalArgumentException()
                }
            }.let { it.first * it.second }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 900)

    val input = readInput("Day02")

    println(part1(input))
    println(part2(input))

    val speedPart1 = measureTimeMillis { repeat(1000) { part1(input) } }
    val speedPart2 = measureTimeMillis { repeat(1000) { part2(input) } }
    println("part1: ${speedPart1}ms")
    println("part2: ${speedPart2}ms")
}

