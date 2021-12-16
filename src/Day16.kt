import kotlin.system.measureTimeMillis

fun main() {
    fun ArrayDeque<Char>.binaryToPacket(): Packet {
        val version = this.removeFirst(3).joinToString("").toInt(2)
        val type = this.removeFirst(3).joinToString("").toInt(2)
        if (type == 4) {
            var last = false
            var literal = ""
            while (!last) {
                if (this.first() == '0') last = true
                literal += this.removeFirst(5).drop(1).joinToString("")
            }
            return LiteralPacket(version, type, literal.toLong(2))
        } else {
            val lengthType = if (this.removeFirst() == '0') LengthType.LENGTH else LengthType.AMOUNT
            val currentPacket = OperatorPacket(version, type, lengthType)

            if (lengthType == LengthType.LENGTH) {
                val length = this.removeFirst(15).joinToString("").toInt(2)
                val toCheck = ArrayDeque(this.removeFirst(length))
                while (toCheck.isNotEmpty()) {
                    currentPacket.addChild(toCheck.binaryToPacket())
                }
            }

            if (lengthType == LengthType.AMOUNT) {
                val repetitions = this.removeFirst(11).joinToString("").toInt(2)
                repeat(repetitions) {
                    currentPacket.addChild(this.binaryToPacket())
                }
            }
            return currentPacket
        }
    }

    fun part1(input: List<String>): Long {
        val bitString =
            ArrayDeque(input.first().map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("").toList())
        val root = bitString.binaryToPacket()

        return root.totalVersion
    }

    fun part2(input: List<String>): Long {
        val bitString =
            ArrayDeque(input.first().map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("").toList())
        val root = bitString.binaryToPacket()

        return root.evaluate()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    //check(part1(testInput) == 15)
    //check(part2(testInput) == 1134)

    val input = readInput("Day16")
    var result: Any

    println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result: $result, expected: 6")
    println("Test 2 solved in ${measureTimeMillis { result = part2(listOf("C200B40A82")) }}ms with result: $result, expected: 3")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
    //println("Part 2 solved in ${measureTimeMillis { result = part2(readInput("Day16_Fabian")) }}ms with result: $result")
}

sealed class Packet(version: Int, type: Int) {
    abstract val totalVersion: Long
    abstract fun evaluate(): Long
}

data class LiteralPacket(val version: Int, val type: Int, val value: Long) : Packet(version, type) {
    override val totalVersion: Long
        get() = version.toLong()

    override fun evaluate(): Long = value
}

data class OperatorPacket(val version: Int, val type: Int, val lengthType: LengthType) : Packet(version, type) {
    private val subPackets = mutableListOf<Packet>()

    fun addChild(child: Packet) {
        subPackets.add(child)
    }

    override val totalVersion: Long
        get() = subPackets.sumOf { it.totalVersion } + version

    override fun evaluate(): Long = when (type) {
        0 -> subPackets.sumOf { it.evaluate() }
        1 -> subPackets.fold(1L) {acc, packet -> acc * packet.evaluate() }
        2 -> subPackets.minOf { it.evaluate() }
        3 -> subPackets.maxOf { it.evaluate() }
        5 -> if (subPackets[0].evaluate() > subPackets[1].evaluate()) 1L else 0L
        6 -> if (subPackets[0].evaluate() < subPackets[1].evaluate()) 1L else 0L
        7 -> if (subPackets[0].evaluate() == subPackets[1].evaluate()) 1L else 0L
        else -> throw IllegalStateException()
    }

    override fun toString(): String {
        return "OperatorPacket(version=$version, type=$type, lengthType=$lengthType, evaluate=${evaluate()})\n$subPackets"
    }
}

enum class LengthType() {
    LENGTH,
    AMOUNT
}

/**
 * Removes the first n elements from this deque and returns those n elements as a list.
 */
fun ArrayDeque<Char>.removeFirst(n: Int): List<Char> {
    val temp = mutableListOf<Char>()
    repeat(n) { temp.add(this.removeFirst()) }
    return temp
}
