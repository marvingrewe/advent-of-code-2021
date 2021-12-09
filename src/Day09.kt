import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val caveFloor = input.map { it.map(Char::digitToInt) }
        var totalRiskLevel = 0
        for (y in caveFloor.indices) {
            for (x in caveFloor[y].indices) {
                val adjacent = listOfNotNull(
                    caveFloor.getSafe(x, y + 1),
                    caveFloor.getSafe(x, y - 1),
                    caveFloor.getSafe(x + 1, y),
                    caveFloor.getSafe(x - 1, y)
                )
                if (adjacent.all { it > caveFloor[y][x] }) totalRiskLevel += caveFloor[y][x] + 1
            }
        }
        return totalRiskLevel
    }

    fun part2(input: List<String>): Int {
        val caveFloor = input.map { it.map(Char::digitToInt) }
        // find all the low points
        val lowPoints = mutableListOf<Pair<Int, Int>>()
        for (y in caveFloor.indices) {
            for (x in caveFloor[y].indices) {
                val adjacent = listOfNotNull(
                    caveFloor.getSafe(x, y + 1),
                    caveFloor.getSafe(x, y - 1),
                    caveFloor.getSafe(x + 1, y),
                    caveFloor.getSafe(x - 1, y)
                )
                if (adjacent.all { it > caveFloor[y][x] }) lowPoints.add(Pair(x, y))
            }
        }
        // find all the basins
        val basins = lowPoints.map { mutableSetOf(it) }.toMutableList()
        for (currentBasin in basins) {
            val toCheck = currentBasin.toMutableSet()
            for (i in 0..8) {
                val newlyFound = mutableSetOf<Pair<Int, Int>>()
                for ((x, y) in toCheck) {
                    val adjacent = listOfNotNull(
                        caveFloor.getSafePoint(x, y + 1),
                        caveFloor.getSafePoint(x, y - 1),
                        caveFloor.getSafePoint(x + 1, y),
                        caveFloor.getSafePoint(x - 1, y)
                    )
                    // TODO: everything about this line
                    newlyFound.addAll(adjacent.filter { caveFloor[it.second][it.first] > caveFloor[y][x] && caveFloor[it.second][it.first] != 9 })
                }
                currentBasin.addAll(toCheck).also { toCheck.clear() }
                toCheck.addAll(newlyFound)
            }
        }
        return basins.sortedBy { it.size }.takeLast(3).fold(1) { acc, mutableSet -> acc * mutableSet.size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    var result: Any

    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}

fun List<List<Int>>.getSafe(x: Int, y: Int): Int? =
    this.getOrNull(y)?.getOrNull(x)

fun List<List<Int>>.getSafePoint(x: Int, y: Int): Pair<Int, Int>? =
    if (this.getOrNull(y)?.getOrNull(x) != null) {
        Pair(x, y)
    } else {
        null
    }
