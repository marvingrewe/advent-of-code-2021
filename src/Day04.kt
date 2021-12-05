fun main() {

    fun List<String>.toBoard(): BingoBoard = this.map { line ->
        line.trim().split("""\W+""".toRegex()).map { Pair(it.toInt(), false) }.toTypedArray()
    }.toTypedArray()

    fun BingoBoard.mark(drawnNumber: Int): Boolean {
        for (i in this.indices) {
            for (j in this[i].indices) {
                val cell = this[i][j]
                if (cell.first == drawnNumber) {
                    this[i][j] = cell.copy(second = true)
                    val checkRow = this[i].all { it.second }
                    val checkColumn = this.all { it[j].second }
                    return checkRow || checkColumn
                }
            }
        }
        return false
    }

    fun BingoBoard.sumOfUnmarked(): Int {
        var sum = 0
        for (i in this.indices) {
            for (j in this[i].indices) {
                if (!this[i][j].second) sum += this[i][j].first
            }
        }
        //println("sumOfUnmarked: $sum")
        return sum
    }

    fun part1(input: List<String>): Int {
        val numbers = input[0].split(",").map(String::toInt)
        val boards = input.asSequence().drop(2).filter { it.isNotBlank() }.chunked(5).map(List<String>::toBoard).toList()
        for (currentNumber in numbers) {
            for (currentBoard in boards) {
                if (currentBoard.mark(currentNumber)) return currentBoard.sumOfUnmarked() * currentNumber//.also { println("currentNumber: $currentNumber") }
            }
        }
        return input.size
    }

    fun part2(input: List<String>): Int {
        val numbers = input[0].split(",").map(String::toInt)
        val boards =
            input.asSequence().drop(2).filter { it.isNotBlank() }.chunked(5).map(List<String>::toBoard).toMutableList()
        for (currentNumber in numbers) {
            if (boards.size > 1) boards.removeIf { it.mark(currentNumber) }
            if (boards.size == 1 && boards[0].mark(currentNumber)) return boards[0].sumOfUnmarked() * currentNumber
        }
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")

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

typealias BingoBoard = Array<Array<Pair<Int, Boolean>>>
