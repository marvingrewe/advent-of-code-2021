fun main() {
    fun part1(input: List<String>): Int {
        val dataLength = input[0].length
        val count = List(dataLength) { 0 }.toMutableList()
        for (i in 0 until dataLength) {
            for (data in input) {
                count[i] += if (data[i] == '1') 1 else -1
            }
        }
        val gam = StringBuilder()
        val eps = StringBuilder()
        count.map {
            gam.append(if (it > 0) 1 else 0)
            eps.append(if (it > 0) 0 else 1)
        }
        return gam.toString().toInt(2) * eps.toString().toInt(2)
    }

    fun part2(input: List<String>): Int {
        val dataLength = input[0].length
        val oxyCount = List(dataLength) { 0 }.toMutableList()
        val co2Count = List(dataLength) { 0 }.toMutableList()
        var oxyInput = input
        var co2Input = input
        for (i in 0 until dataLength) {
            for (data in oxyInput) {
                oxyCount[i] += if (data[i] == '1') 1 else -1
            }
            oxyInput = oxyInput.filter {
                it[i] == when (oxyCount[i] >= 0) {
                    true -> '1'
                    false -> '0'
                }
            }
        }
        for (i in 0 until dataLength) {
            for (data in co2Input) {
                co2Count[i] += if (data[i] == '1') 1 else -1
            }
            co2Input = co2Input.filter {
                it[i] == when (co2Count[i] >= 0) {
                    true -> '0'
                    false -> '1'
                }
            }
            if (co2Input.size == 1) break
        }
        //println("oxyInput: $oxyInput")
        //println("co2Input: $co2Input")
        return oxyInput[0].toInt(2) * co2Input[0].toInt(2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")

    val start = System.currentTimeMillis()
    val part1 = part1(input)
    val middle = System.currentTimeMillis()
    val part2 = part2(input)
    val end = System.currentTimeMillis()

    println("part1: ${middle - start}ms")
    println("part2: ${end - middle}ms")
    println(part1)
    println(part2)
}
