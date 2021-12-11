import kotlin.IllegalArgumentException
import kotlin.system.measureTimeMillis

fun main() {
    val openingBrackets = listOf('(', '[', '{', '<')
    val closingBrackets = listOf(')', ']', '}', '>')
    val brackets = openingBrackets.mapIndexed { index, _ -> Pair(openingBrackets[index], closingBrackets[index]) }

    fun part1(input: List<String>): Int {
        val openChunks = ArrayDeque<Char>()
        var errorScore = 0
        for (line in input) {
            for (character in line) {
                if (character in openingBrackets) openChunks.add(character)
                else if (Pair(openChunks.last(), character) in brackets) openChunks.removeLast()
                else if (Pair(openChunks.last(), character) !in brackets && character in closingBrackets) {
                    errorScore += when (character) {
                        ')' -> 3
                        ']' -> 57
                        '}' -> 1197
                        '>' -> 25137
                        else -> throw IllegalArgumentException()
                    }
                    break
                }
            }
        }
        return errorScore
    }

    fun part2(input: List<String>): Long {
        val scores = mutableListOf<Long>()
        for (line in input) {
            val openChunks = ArrayDeque<Char>()
            for (character in line) {
                if (character in openingBrackets) openChunks.add(character)
                else if (Pair(openChunks.last(), character) in brackets) openChunks.removeLast()
                else if (Pair(openChunks.last(), character) !in brackets && character in closingBrackets) {
                    openChunks.clear()
                    break
                }
            }
            openChunks.reverse()
            scores.add(openChunks
                .fold(0) { score, character ->
                    score * 5 + when (character) {
                        '(' -> 1
                        '[' -> 2
                        '{' -> 3
                        '<' -> 4
                        else -> throw IllegalArgumentException()
                    }
                }
            )
        }
        val validScores = scores.filter { it != 0L }
        return validScores.sorted()[validScores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    var result: Any

    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}
