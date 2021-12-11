import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val octopodes = input.map { it.map(Char::digitToInt).toMutableList() }.toMutableList()
        var flashes = 0
        //println("${octopodes.joinToString("\n")}\n")
        repeat(100) {
            val activeOctopodes = mutableSetOf<Pair<Int, Int>>()
            val hasFlashed = mutableSetOf<Pair<Int, Int>>()
            for (y in octopodes.indices) {
                for (x in octopodes[y].indices) {
                    octopodes[y][x] += 1
                    if (octopodes[y][x] > 9) activeOctopodes.add(Pair(x, y))
                }
            }
            while (activeOctopodes.isNotEmpty()) {
                val currentOctopus = activeOctopodes.first()
                val (x, y) = currentOctopus
                listOfNotNull(
                    octopodes.getSafePoint(x - 1, y), // left
                    octopodes.getSafePoint(x + 1, y), // right
                    octopodes.getSafePoint(x, y - 1), // up
                    octopodes.getSafePoint(x, y + 1), // down
                    octopodes.getSafePoint(x - 1, y - 1), // up left
                    octopodes.getSafePoint(x + 1, y - 1), // up right
                    octopodes.getSafePoint(x - 1, y + 1), // down left
                    octopodes.getSafePoint(x + 1, y + 1), // down right
                ).map { (x, y) ->
                    if (Pair(x, y) !in hasFlashed) {
                        octopodes[y][x] += 1
                        if (octopodes[y][x] > 9) activeOctopodes.add(Pair(x, y))
                    }
                }
                flashes++
                octopodes[y][x] = 0
                hasFlashed.add(currentOctopus)
                activeOctopodes.remove(currentOctopus)
            }
            //println("${octopodes.joinToString("\n")}\n")
        }
        return flashes
    }

    fun part2(input: List<String>): Int {
        val octopodes = input.map { it.map(Char::digitToInt).toMutableList() }.toMutableList()
        //println("${octopodes.joinToString("\n")}\n")
        var synchronizedFlash = false
        var step = 0
        while (!synchronizedFlash) {
            val activeOctopodes = mutableSetOf<Pair<Int, Int>>()
            val hasFlashed = mutableSetOf<Pair<Int, Int>>()
            for (y in octopodes.indices) {
                for (x in octopodes[y].indices) {
                    octopodes[y][x] += 1
                    if (octopodes[y][x] > 9) activeOctopodes.add(Pair(x, y))
                }
            }
            while (activeOctopodes.isNotEmpty()) {
                val currentOctopus = activeOctopodes.first()
                val (x, y) = currentOctopus
                listOfNotNull(
                    octopodes.getSafePoint(x - 1, y), // left
                    octopodes.getSafePoint(x + 1, y), // right
                    octopodes.getSafePoint(x, y - 1), // up
                    octopodes.getSafePoint(x, y + 1), // down
                    octopodes.getSafePoint(x - 1, y - 1), // up left
                    octopodes.getSafePoint(x + 1, y - 1), // up right
                    octopodes.getSafePoint(x - 1, y + 1), // down left
                    octopodes.getSafePoint(x + 1, y + 1), // down right
                ).map { (x, y) ->
                    if (Pair(x, y) !in hasFlashed) {
                        octopodes[y][x] += 1
                        if (octopodes[y][x] > 9) activeOctopodes.add(Pair(x, y))
                    }
                }
                octopodes[y][x] = 0
                hasFlashed.add(currentOctopus)
                activeOctopodes.remove(currentOctopus)
            }
            step++
            if (hasFlashed.size == octopodes.size * octopodes[0].size) synchronizedFlash = true
            //println("${octopodes.joinToString("\n")}\n")
        }
        return step
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    var result: Any

    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}
