import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    fun String.toSnailfishNumber(): SnailfishNumber {
        if (this.length == 1) return SnailfishNumber(this.first().digitToInt())
        var depth = 0
        for ((index, char) in this.withIndex()) {
            when (char) {
                '[' -> depth++
                ']' -> depth--
                ',' -> {
                    if (depth == 1) return SnailfishNumber(
                        leftChild = substring(1, index).toSnailfishNumber(),
                        rightChild = substring(index + 1, lastIndex).toSnailfishNumber()
                    )
                }
            }
        }
        return SnailfishNumber()
    }

    fun part1(input: List<String>): Int {
        val currentLine = input.first()
        var root = currentLine.toSnailfishNumber()
        for (i in 1..input.lastIndex) {
            root += input[i].toSnailfishNumber()
        }
        return root.magnitude
    }

    fun part2(input: List<String>): Int {
        var max = Int.MIN_VALUE
        for (i in input.indices) {
            for (j in input.indices) {
                if (i != j) {
                    val current = (input[i].toSnailfishNumber() + input[j].toSnailfishNumber()).magnitude
                    max = max(max, current)
                }
            }
        }
        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    //check(part1(testInput) == 15)
    //check(part2(testInput) == 1134)

    val input = readInput("Day18")
    var result: Any

    println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result $result, expected 4140")
    println("Test 2 solved in ${measureTimeMillis { result = part2(testInput) }}ms with result $result, expected 3993")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result $result")
}

class SnailfishNumber(
    var value: Int? = null,
    leftChild: SnailfishNumber? = null,
    rightChild: SnailfishNumber? = null,
    var parent: SnailfishNumber? = null,
) {
    init {
        leftChild?.parent = this
        rightChild?.parent = this
    }

    var leftChild: SnailfishNumber? = leftChild
        set(value) {
            value?.parent = this
            field = value
        }

    var rightChild: SnailfishNumber? = rightChild
        set(value) {
            value?.parent = this
            field = value
        }

    private fun reduce() {
        var changed = true
        while (changed) {
            var explodeChanged = true
            while (explodeChanged) explodeChanged = explodeIfNeeded()
            changed = splitIfNeeded()
        }
    }

    operator fun plus(other: SnailfishNumber): SnailfishNumber {
        return SnailfishNumber(leftChild = this, rightChild = other).apply { this.reduce() }
    }

    private fun explodeIfNeeded(remainingDepth: Int = 4): Boolean {
        if (remainingDepth == 0 && value == null) {
            val parent = checkNotNull(parent)
            val left = checkNotNull(leftChild?.value)
            val right = checkNotNull(rightChild?.value)
            parent.addUpLeft(left, this)
            parent.addUpRight(right, this)
            leftChild = null
            rightChild = null
            value = 0
            return true
        }

        val left = leftChild
        val right = rightChild
        if (left != null && right != null)
            return (left.explodeIfNeeded(remainingDepth - 1) || right.explodeIfNeeded(remainingDepth - 1))

        return false
    }

    private fun addUpLeft(value: Int, previous: SnailfishNumber) {
        val left = checkNotNull(leftChild)
        if (left !== previous)
            left.addDownLeft(value)
        else parent?.addUpLeft(value, this)
    }

    private fun addDownLeft(value: Int) {
        if (this.value != null)
            this.value += value
        else rightChild?.addDownLeft(value)
    }

    private fun addUpRight(value: Int, previous: SnailfishNumber) {
        val right = checkNotNull(rightChild)
        if (right !== previous)
            right.addDownRight(value)
        else parent?.addUpRight(value, this)
    }

    private fun addDownRight(value: Int) {
        if (this.value != null)
            this.value += value
        else leftChild?.addDownRight(value)
    }

    private fun splitIfNeeded(): Boolean {
        val valueToSplit = value
        val leftChildToSplit = leftChild
        val rightChildToSplit = rightChild
        if (valueToSplit != null && valueToSplit > 9) {
            leftChild = SnailfishNumber(valueToSplit / 2)
            rightChild = SnailfishNumber((valueToSplit + 1) / 2)
            value = null
            return true
        } else if (leftChildToSplit != null && rightChildToSplit != null) {
            return (leftChildToSplit.splitIfNeeded() || rightChildToSplit.splitIfNeeded())
        }
        return false
    }

    val magnitude: Int
        get() {
            val leftChildToAdd = leftChild
            val rightChildToAdd = rightChild
            return value
                ?: if (leftChildToAdd != null && rightChildToAdd != null) 3 * leftChildToAdd.magnitude + 2 * rightChildToAdd.magnitude
                else throw IllegalStateException()
        }

    override fun toString(): String {
        return if (value != null) value.toString()
        else "[$leftChild,$rightChild]"
    }
}
