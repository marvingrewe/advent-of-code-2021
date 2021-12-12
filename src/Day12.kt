import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val caves = input.flatMap { it.split("-") }.toSet()
        val connections = caves.associateWith { mutableSetOf<String>() }
        for (cavePair in input) {
            val (first, second) = cavePair.split("-")
            connections[first]!!.add(second)
            connections[second]!!.add(first)
        }
        val path = ArrayDeque<String>()
        val paths = mutableListOf<String>()
        val visited = caves.associateWith { false }.toMutableMap()

        fun dfs(node: String) {
            // add self to path
            path.add(node)
            if (node == "end") {
                paths.add(path.toString())
                path.removeLast()
                return
            }
            if (node.all(Char::isLowerCase)) visited[node] = true

            val toVisit = connections[node]!!.filter { !visited[it]!! }.toMutableList()
            while (toVisit.isNotEmpty()) {
                val nextNode = toVisit.first()
                dfs(nextNode)
                toVisit.remove(nextNode)
            }

            visited[node] = false

            // remove self from path
            path.removeLast()
        }

        dfs("start")

        return paths.size
    }

    fun part2(input: List<String>): Int {
        val caves = input.flatMap { it.split("-") }.toSet()
        val connections = caves.associateWith { mutableSetOf<String>() }
        for (cavePair in input) {
            val (first, second) = cavePair.split("-")
            connections[first]!!.add(second)
            connections[second]!!.add(first)
        }
        val path = ArrayDeque<String>()
        val paths = mutableSetOf<String>()
        val visited = caves.associateWith { false }.toMutableMap()
        val smallCaves = caves.filter { it[0].isLowerCase() }.filter { it != "start" && it != "end" }
        var firstVisitSmollishCave = true

        fun dfs(node: String, visitTwice: String) {
            // add self to path
            path.add(node)
            if (node == "end") {
                paths.add(path.toString())
                path.removeLast()
                return
            }
            if (node.all(Char::isLowerCase)) {
                if (firstVisitSmollishCave && node == visitTwice) firstVisitSmollishCave = false
                else visited[node] = true
            }

            val toVisit = connections[node]!!.filter { !visited[it]!! }.toMutableList()
            while (toVisit.isNotEmpty()) {
                val nextNode = toVisit.first()
                dfs(nextNode, visitTwice)
                toVisit.remove(nextNode)
            }

            if (node[0].isLowerCase() && node != "end" && !visited[node]!!) firstVisitSmollishCave = true
            visited[node] = false

            // remove self from path
            path.removeLast()
        }

        for (smol in smallCaves) {
            firstVisitSmollishCave = true
            dfs("start", smol)
        }

        return paths.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    //check(part1(testInput) == 19)
    //check(part2(testInput) == 103)

    val input = readInput("Day12")
    var result: Any

    println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result: $result, expected: 19")
    println("Test 2 solved in ${measureTimeMillis { result = part2(testInput) }}ms with result: $result, expected: 103")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}
