import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("inputs", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Grants safe access to int-value in 2D array
 */
fun List<List<Int>>.getSafe(x: Int, y: Int): Int? =
    this.getOrNull(y)?.getOrNull(x)

/**
 * Grants safe access to coordinates of 2D array
 */
fun List<List<Int>>.getSafePoint(x: Int, y: Int): Pair<Int, Int>? =
    if (this.getOrNull(y)?.getOrNull(x) != null) {
        Pair(x, y)
    } else {
        null
    }

/**
 * Implementations of plus and minus operations on Int?, mostly for easy incrementation and decrementation of map values
 */
operator fun Int?.plus(other: Int): Int = this?.plus(other) ?: other
operator fun Int?.minus(other: Int): Int = this?.minus(other) ?: -other

fun heuristicDistance(start: Point, end: Point): Int {
    val dx = abs(start.first - end.first)
    val dy = abs(start.second - end.second)
    return (dx + dy)
}

/**
 * A*-algorithm, mostly from https://rosettacode.org/wiki/A*_search_algorithm#Kotlin
 */
fun aStar(start: Point, finish: Point, grid: Grid): Int {
    val openVertices = mutableSetOf(start)
    val closedVertices = mutableSetOf<Point>()
    val costFromStart = mutableMapOf(start to 0)
    val estimatedTotalCost = mutableMapOf(start to heuristicDistance(start, finish))

    val cameFrom = mutableMapOf<Point, Point>() // Used to generate path by back tracking

    while (openVertices.isNotEmpty()) {

        val currentPos = openVertices.minByOrNull { estimatedTotalCost.getValue(it) }!!

        // Check if we have reached the finish
        if (currentPos == finish) {
            return estimatedTotalCost.getValue(finish)
        }

        //if (currentPos.first % 100 == 0 || currentPos.second % 100 == 0) println("currentPos: $currentPos")

        // Mark current vertex as closed
        openVertices.remove(currentPos)
        closedVertices.add(currentPos)

        grid.getNeighbours(currentPos)
            .filterNot { closedVertices.contains(it) } // Exclude previous visited vertices
            .forEach { neighbour ->
                val score = costFromStart.getValue(currentPos) + grid.moveCost(neighbour)
                if (score < costFromStart.getOrDefault(neighbour, Int.MAX_VALUE)) {
                    openVertices.add(neighbour)
                    cameFrom[neighbour] = currentPos
                    costFromStart[neighbour] = score
                    estimatedTotalCost[neighbour] = score + heuristicDistance(neighbour, finish)
                }
            }
    }

    throw IllegalArgumentException("No Path from Start $start to Finish $finish")
}

fun Grid.getNeighbours(position: Point) : List<Point> = listOfNotNull(
        this.getSafePoint(position.first, position.second + 1),
        this.getSafePoint(position.first, position.second - 1),
        this.getSafePoint(position.first + 1, position.second),
        this.getSafePoint(position.first - 1, position.second)
    )

fun Grid.moveCost(to: Point) = this[to.second][to.first]

typealias Line = Pair<Point, Point>

typealias Grid = List<List<Int>>

typealias Point = Pair<Int, Int>

typealias BingoBoard = Array<Array<Pair<Int, Boolean>>>
