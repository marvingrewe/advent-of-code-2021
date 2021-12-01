fun main() {
    fun part1(rawInput: List<String>): Int {
        val input = rawInput.map(String::toInt)
        var depthIncreases = 0
        for (i in 1 until input.size) {
            if (input[i] > input[i - 1]) depthIncreases++
        }
        return depthIncreases
    }

    fun part2(rawInput: List<String>): Int {
        val input = rawInput.map(String::toInt)
        var depthIncreases = 0
        var previousWindow = input[0] + input[1] + input[2]
        for (i in 3 until input.size) {
            val currentWindow = input[i] + input[i - 1] + input[i - 2]
            if (currentWindow > previousWindow) depthIncreases++
            previousWindow = currentWindow
        }
        return depthIncreases
    }

    fun part2func(rawInput: List<String>): Int {
        val input = rawInput.map(String::toInt)
        return input.windowed(3, 1).map { it.sum() }.zipWithNext().count { it.first < it.second }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
    println(part2func(input))
}
