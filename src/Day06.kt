import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val fishList = input[0].split(",").map(String::toInt).toMutableList()
        for (i in 1..80) {
            val newFish = emptyList<Int>().toMutableList()
            for (j in fishList.indices) {
                if (fishList[j] > 0) {
                    fishList[j]--
                } else {
                    fishList[j] = 6
                    newFish.add(8)
                }
            }
            fishList.addAll(newFish)
            //println("newFish: $newFish")
            //println("after $i days: ${fishList}")
        }
        return fishList.size
    }

    fun part2(input: List<String>): Long {
        val fishList = input[0].split(",").map(String::toInt).toMutableList()
        val listOfStartingFishByAge = fishList.groupBy { it }.mapValues { it.value.size }.toSortedMap()
        println("Initial, $listOfStartingFishByAge")
        val listOfFishAfterSimulation = emptyMap<Int,Map<Int, Int>>().toMutableMap()
        for (t in 0..8) {
            val fishSimulation = listOf(t).toMutableList()
            var numOfFishByAge = emptyMap<Int, Int>()
            for (i in 1..128) {
                val newFish = emptyList<Int>().toMutableList()
                for (j in fishSimulation.indices) {
                    if (fishSimulation[j] > 0) {
                        fishSimulation[j]--
                    } else {
                        fishSimulation[j] = 6
                        newFish.add(8)
                    }
                }
                fishSimulation.addAll(newFish)
                numOfFishByAge = fishSimulation.groupBy { it }.mapValues { it.value.size }.toSortedMap()
            }
            listOfFishAfterSimulation[t] = numOfFishByAge
        }
        val numOfFishAfterSimulation = listOfFishAfterSimulation.mapValues { it.value.values.sum().toLong() }
        val listOfTotalFishByAge = emptyMap<Int, Long>().toMutableMap()
        for (t in 0..8) {
            val count = listOfFishAfterSimulation[t]!!.map { (key, value) ->
                value * numOfFishAfterSimulation[key]!!.toLong()
            }.sum()
            listOfTotalFishByAge[t] = count
        }
        val numOfTotalFish = listOfStartingFishByAge.map { (key, value) ->
            value * listOfTotalFishByAge[key]!!
        }.sum()
        return numOfTotalFish
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val oneFish = readInput("Day06_oneFish")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539)
    println(part1(testInput))

    val input = readInput("Day06")
    var result = 0
    var longResult: Long = 0

    val speedPart1 = measureTimeMillis { result = part1(input) }
    println("part1 finished in ${speedPart1}ms with result $result")
    val speedPart2 = measureTimeMillis { longResult = part2(input) }
    println("part2 finished in ${speedPart2}ms with result $longResult")

}
