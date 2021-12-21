import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    data class Player(var pos: Int, var score: Int = 0)

    val permutations = mapOf(
        3 to 1,
        4 to 3,
        5 to 6,
        6 to 7,
        7 to 6,
        8 to 3,
        9 to 1
    )

    fun part1(input: List<String>): Int {
        val player1 = Player(input[0].last().digitToInt())
        val player2 = Player(input[1].last().digitToInt())
        val die = generateSequence { 1..100 }.flatten().chunked(3).map { it.sum() }.iterator()
        val turnOrder = generateSequence { listOf(player1, player2) }.flatten().iterator()
        var diceRolls = 0
        while (player1.score < 1000 && player2.score < 1000) {
            with(turnOrder.next()) {
                pos = (pos + die.next() - 1) % 10 + 1
                score += pos
                diceRolls += 3
            }
        }
        return min(player1.score, player2.score) * diceRolls
    }

    fun dfs(
        depth: Int,
        worlds: Long,
        player: Player,
        wonWorlds: MutableMap<Int, Long>,
        notYetWonWorlds: MutableMap<Int, Long>
    ) {
        if (player.score >= 21) {
            wonWorlds[depth] += worlds
        } else {
            notYetWonWorlds[depth] += worlds
            for (permutation in permutations) {
                val newPos = (player.pos + permutation.key - 1) % 10 + 1
                val newScore = player.score + newPos
                dfs(
                    depth + 1,
                    worlds * permutation.value,
                    player.copy(pos = newPos, score = newScore),
                    wonWorlds,
                    notYetWonWorlds
                )
            }
        }
    }

    fun part2(input: List<String>): Long {
        val player1 = Player(input[0].last().digitToInt())
        val player2 = Player(input[1].last().digitToInt())
        var p1Worlds = 0L
        var p2Worlds = 0L
        val p1Permutations = mutableMapOf<Int, Long>()
        val p2Permutations = mutableMapOf<Int, Long>()
        val p1NotWonPermutations = mutableMapOf<Int, Long>()
        val p2NotWonPermutations = mutableMapOf<Int, Long>()

        dfs(0, 1, player1, p1Permutations, p1NotWonPermutations)
        dfs(0, 1, player2, p2Permutations, p2NotWonPermutations)

        for (i in p1Permutations.keys) {
            p1Worlds += (p1Permutations[i] ?: 1) * (p2NotWonPermutations[i - 1] ?: 1)
        }
        for (i in p2Permutations.keys) {
            p2Worlds += (p1NotWonPermutations[i] ?: 1) * (p2Permutations[i] ?: 1)
        }
        return max(p1Worlds, p2Worlds)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    //check(part1(testInput) == 15)
    //check(part2(testInput) == 1134)

    val input = readInput("Day21")
    var result: Any

    println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result $result, expected 739785")
    println("Test 2 solved in ${measureTimeMillis { result = part2(testInput) }}ms with result $result, expected 444356092776315")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}
