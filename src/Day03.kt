fun main() {
    fun part1(input: List<String>): Int {
        val dataLength = input[0].length
        val count = List(dataLength) { 0 }.toMutableList()
        println(count)
        for (data in input) {
            for (i in 0 until dataLength) {
                count[i] += if (data[i] == '1') 1 else -1
            }
        }
        val gam = StringBuilder()
        val eps = StringBuilder()
        count.map {
            gam.append( if (it > 0) 1 else 0)
            eps.append( if (it > 0) 0 else 1)
        }
        println(count)
        println("$gam = ${gam.toString().toInt(2)}")
        println("$eps = ${eps.toString().toInt(2)}")
        return gam.toString().toInt(2) * eps.toString().toInt(2)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)

    val input = readInput("Day03")
    println(part1(input))
    //println(part2(input))

    //println("01001".toUInt(2))
}
