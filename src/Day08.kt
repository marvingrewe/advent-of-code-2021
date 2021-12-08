import kotlin.system.measureTimeMillis

fun main() {
    val obviousNumbers = listOf(2, 3, 4, 7)

    fun part1(input: List<String>): Int = input
        .flatMap { it.split(" | ")[1].split(" ") }
        .count { it.length in obviousNumbers }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (i in input.indices) {
            val (pattern, output) = input[i].split(" | ").map { s -> s.split(" ").map(String::toSet) }
            val code = mutableMapOf<Int, Set<Char>>()
            // first round of deduction: obvious numbers (1, 4, 7, 8)
            for (it in pattern) {
                when (it.size) {
                    2 -> code[1] = it
                    4 -> code[4] = it
                    3 -> code[7] = it
                    7 -> code[8] = it
                }
            }
            // second round of deduction: 1 -> 3, 4 -> 9
            for (it in pattern subtract code.values.toSet()) {
                if (it.size == 5 && it.containsAll(code[1]!!)) code[3] = it
                if (it.size == 6 && it.containsAll(code[4]!!)) code[9] = it
            }
            // third round of deduction: 0 and 6 with size and 1
            for (it in pattern subtract code.values.toSet()) {
                if (it.size == 6 && it.containsAll(code[1]!!)) code[0] = it
                if (it.size == 6 && !it.containsAll(code[1]!!)) code[6] = it
            }
            // fourth round of deduction: 6 -> 5
            for (it in pattern subtract code.values.toSet())
                if (code[6]!!.containsAll(it)) code[5] = it
            // last round of deduction: 2 is last one missing
            for (it in pattern subtract code.values.toSet())
                code[2] = it

            if (i == 0) println("$code")
            val invCode = mutableMapOf<Set<Char>, Int>()
            for (it in code) {
                invCode[it.value] = it.key
            }

            sum += output.map { invCode[it] }.joinToString("").toInt()

        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    var result: Any

    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}
