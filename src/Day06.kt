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
        //println("Initial, $listOfStartingFishByAge")
        val listOfFishAfterSimulation = emptyMap<Int, Map<Int, Int>>().toMutableMap()
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

    fun part2alt(input: List<String>, days: Int): Long {
        val inputFish = input[0].split(",").map(String::toInt).groupingBy { it }.eachCount()
        val fish = LongArray(9)
        for (i in 0..8) fish[i] = inputFish[i]?.toLong() ?: 0
        for (i in 0 until days) {
            /*
            val newFish = fish[0]
            for (j in 0 until 8) {
                fish[j] = fish[j + 1]
            }
            fish[8] = newFish
            fish[6] += newFish
             */
            fish[(i + 7) % 9] += fish[i % 9]
            //println("Day $i: ${fish.contentToString()}")
        }
        return fish.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    //check(part2alt(testInput, 80) == 5934L)
    //check(part2alt(testInput, 256) == 26984457539)

    val input = readInput("Day06")
    var result: Any

    //println("Part 1 solved in ${measureTimeMillis { result = part2alt(input, 80) }}ms with result: $result")
    //println("Part 2 solved in ${measureTimeMillis { result = part2alt(input, 256) }}ms with result: $result")
    println("Part 3 solved in ${measureTimeMillis { result = part2alt(input, 434) }}ms with result: $result")

}
