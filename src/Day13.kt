import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val dots = input.takeWhile(String::isNotBlank)
            .map { with(it.split(",")) { Pair(component1().toInt(), component2().toInt()) } }.toMutableSet()
        val folds = input.takeLastWhile(String::isNotBlank)
        dots.foldPaper(folds[0])
        return dots.size
    }

    fun part2(input: List<String>): Int {
        val dots = input.takeWhile(String::isNotBlank)
            .map { with(it.split(",")) { Pair(component1().toInt(), component2().toInt()) } }.toMutableSet()
        val folds = input.takeLastWhile(String::isNotBlank)

        for (instruction in folds) {
            dots.foldPaper(instruction)
        }
        val output = Array(dots.maxOf { it.second } + 1) { Array(dots.maxOf { it.first } + 1) { "  " } }
        for ((x, y) in dots) {
            output[y][x] = "##"
        }
        println(output.map { line -> line.joinToString("") { it } }.joinToString("\n") { it })
        return output.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    //check(part1(testInput) == 15)
    //check(part2(testInput) == 1134)

    val input = readInput("Day13")
    var result: Any

    println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result: $result, expected: 17")
    println("Test 2 solved in ${measureTimeMillis { result = part2(testInput) }}ms with result: $result, expected: ")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}

fun MutableSet<Pair<Int, Int>>.foldPaper(fold: String) {
    val (alignment, index) = with(fold.split("=")) { Pair(component1().last(), component2().toInt()) }
    if (alignment == 'x') {
        for (point in this.filter { it.first > index }) {
            this.remove(point)
            this.add(Pair(index * 2 - point.first, point.second))
        }
    }
    if (alignment == 'y') {
        for (point in this.filter { it.second > index }) {
            this.remove(point)
            this.add(Pair(point.first, index * 2 - point.second))
        }
    }
}
