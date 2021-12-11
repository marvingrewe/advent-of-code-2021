import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

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
