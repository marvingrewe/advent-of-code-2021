import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val enhancementAlgorithm = input.first().toList()
        var image = input.drop(2).map { it.toList() }
        val iterations = 2
        for (i in 1..iterations) {
            val xSize = image.first().size + 2
            val ySize = image.size + 2
            var enhancedImage = MutableList(ySize) {MutableList(xSize) { ' ' } }
            for (x in 0 until xSize) {
                for (y in 0 until ySize) {
                    enhancedImage[y][x] = image.enhance(x - 1, y - 1, enhancementAlgorithm, fillerChar(i))
                }
            }
            image = enhancedImage
        }
        return image.sumOf { line -> line.count { it == '#' } }
    }

    fun part2(input: List<String>): Int {
        val enhancementAlgorithm = input.first().toList()
        var image = input.drop(2).map { it.toList() }
        val iterations = 50
        for (i in 1..iterations) {
            val xSize = image.first().size + 2
            val ySize = image.size + 2
            var enhancedImage = MutableList(ySize) {MutableList(xSize) { ' ' } }
            for (x in 0 until xSize) {
                for (y in 0 until ySize) {
                    enhancedImage[y][x] = image.enhance(x - 1, y - 1, enhancementAlgorithm, fillerChar(i))
                }
            }
            image = enhancedImage
        }
        return image.sumOf { line -> line.count { it == '#' } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")

    val input = readInput("Day20")
    var result: Any

    // implementation doesn't work with test input, filler char is coded for actual input
    // println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result $result, expected 4140")
    // println("Test 2 solved in ${measureTimeMillis { result = part2(testInput) }}ms with result $result, expected 3993")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result $result")
}

fun List<List<Char>>.enhance(x: Int, y: Int, algo: List<Char>, filler: Char): Char {
    val value = listOf(
        this.getSafe(x - 1, y - 1) ?: filler, // top left
        this.getSafe(x, y - 1) ?: filler, // top middle
        this.getSafe(x + 1, y - 1) ?: filler, // top right
        this.getSafe(x - 1, y) ?: filler, // left
        this.getSafe(x, y) ?: filler, // middle
        this.getSafe(x + 1, y) ?: filler, // right
        this.getSafe(x - 1, y + 1) ?: filler, // bottom left
        this.getSafe(x, y + 1) ?: filler, // bottom middle
        this.getSafe(x + 1, y + 1) ?: filler, // bottom right
    ).map { when (it) {
        '#' -> 1
        '.' -> 0
        else -> throw IllegalStateException()
    } }.joinToString("").toInt(2)
    return algo[value]
}

fun fillerChar(iteration: Int): Char = if (iteration % 2 == 0) '#' else '.'
