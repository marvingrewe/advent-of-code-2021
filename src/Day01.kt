fun main() {
    fun part1(input: List<String>): Int {
        var depthIncreases = 0
        for (i in 1 until input.size) {
            if (input[i].toInt() > input[i - 1].toInt()) depthIncreases++
        }
        return depthIncreases
    }

    fun part2(input: List<String>): Int {
        var depthIncreases = 0
        var previousWindow = input[0].toInt() + input[1].toInt() + input[2].toInt()
        for (i in 3 until input.size) {
            val currentWindow = input[i].toInt() + input[i - 1].toInt() + input[i - 2].toInt()
            if (currentWindow > previousWindow) depthIncreases++
            previousWindow = currentWindow
        }
        return depthIncreases
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
