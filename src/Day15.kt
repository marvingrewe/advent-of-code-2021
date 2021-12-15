import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val cave = input.map { it.map(Char::digitToInt) }
        val xMax = cave[0].lastIndex
        val yMax = cave.lastIndex
        val start = Pair(0, 0)
        val finish = Pair(xMax, yMax)
        return aStar(start, finish, cave)
    }

    fun part2(input: List<String>): Int {
        val initialCave = input.map { it.map(Char::digitToInt).toMutableList() }
        val cave = initialCave.toMutableList()
        for (line in initialCave) {
            val toAdd = mutableListOf<Int>()
            for (i in 1..4) {
                for (pos in line) {
                    val risk = pos + i
                    toAdd.add(if (risk > 9) risk % 9 else risk)
                }
            }
            line.addAll(toAdd)
        }
        for (i in 1..4) {
            for (line in initialCave) {
                val newLine = mutableListOf<Int>()
                for (pos in line) {
                    val risk = pos + i
                    newLine.add(if (risk > 9) risk % 9 else risk)
                }
                cave.add(newLine)
            }
        }
        val xMax = cave[0].lastIndex
        val yMax = cave.lastIndex
        val start = Pair(0, 0)
        val finish = Pair(xMax, yMax)
        println("finish: $finish")
        //println(cave.map { line -> line.joinToString("") { it.toString() } }.joinToString("\n") { it })
        return aStar(start, finish, cave)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    //check(part1(testInput) == 15)
    //check(part2(testInput) == 1134)

    val input = readInput("Day15")
    var result: Any

    println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result: $result, expected: 40")
    println("Test 2 solved in ${measureTimeMillis { result = part2(testInput) }}ms with result: $result, expected: 315")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}
