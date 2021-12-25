import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        var cucumbers = input.map { it.toMutableList() }.toMutableList()
        var changed = true
        var iterations = 0
        while (changed) {
            val eastMoved = cucumbers.map { it.toMutableList() }.toMutableList()
            for (y in cucumbers.indices) {
                for (x in cucumbers[y].indices) {
                    eastMoved[y][x] = when (cucumbers[y][x]) {
                        'v' -> 'v'
                        '>' -> if (cucumbers[y][if (x + 1 <= cucumbers[y].lastIndex) x + 1 else 0] == '.') '.' else '>'
                        '.' -> if (cucumbers[y][if (x - 1 >= 0) x - 1 else cucumbers[y].lastIndex] == '>') '>' else '.'
                        else -> error("can only have 'v', '>' or '.' in list")
                    }
                }
            }
            val bothMoved = eastMoved.map { it.toMutableList() }.toMutableList()
            for (y in eastMoved.indices) {
                for (x in eastMoved[y].indices) {
                    bothMoved[y][x] = when (eastMoved[y][x]) {
                        '>' -> '>'
                        'v' -> if (eastMoved[if (y + 1 <= cucumbers.lastIndex) y + 1 else 0][x] == '.') '.' else 'v'
                        '.' -> if (eastMoved[if (y - 1 >= 0) y - 1 else cucumbers.lastIndex][x] == 'v') 'v' else '.'
                        else -> error("can only have 'v', '>' or '.' in list")
                    }
                }
            }
            if (cucumbers == bothMoved) changed = false else cucumbers = bothMoved
            iterations++
        }
        return iterations
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")

    val input = readInput("Day25")
    var result: Any

    println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result $result, expected 58")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result $result")
}
