import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Long {
        val initialState: Pair<AluState, Long?> = intArrayOf(0, 0, 0, 0) to null
        var states = PriorityQueue<Pair<AluState, Long?>> { o1, o2 ->
            o2.second?.compareTo(o1?.second ?: 0) ?: 0
        }
        states.add(initialState)
        val commandSets = input.chunked(18).map { command -> command.map { it.split(' ') } }
        for ((iterator, commandSet) in commandSets.withIndex()) { // todo: maybe apply operations one by one, special case for input
            val toAdd = PriorityQueue<Pair<AluState, Long?>> { o1, o2 ->
                o2.second?.compareTo(o1?.second ?: 0) ?: 0
            }
            for (state in states) {
                // println("state: ${state.first.contentToString()}, value: ${state.second}")
                for (i in 9 downTo 1) {
                    val new = state.first.copyOf()
                    new.nextAluState(i, commandSet)
                    // println("state: ${new.contentToString()}, ${state.second.concat(i)}")
                    if (toAdd.none { it.first.contentEquals(new) }) {
                        toAdd.add(new to state.second.concat(i))
                    }
                }
            }
            states = toAdd
            println("iteration $iterator of ${commandSets.size}: added ${toAdd.size} new states")
        }
        return states.filter { it.first[3] == 0 }.maxOf { it.second ?: 0 }
        // return sequenceOf(99999999999999 downTo 11111111111111).flatten().find { it.verifyModelNumber(input) } ?: 11111111111111
    }

    fun part1alternate(input: List<String>): Long {
        val initialState: Pair<AluState, Long?> = intArrayOf(0, 0, 0, 0) to null
        var states = PriorityQueue<Pair<AluState, Long?>> { o1, o2 ->
            o2.second?.compareTo(o1?.second ?: 0) ?: 0
        }
        states.add(initialState)
        val commands = input.map { it.split(' ') }
        for ((iterator, command) in commands.withIndex()) {
            val toAdd = PriorityQueue<Pair<AluState, Long?>> { o1, o2 ->
                o2.second?.compareTo(o1?.second ?: 0) ?: 0
            }
            for (state in states) {
                // println("state: ${state.first.contentToString()}, value: ${state.second}")
                if (command.first() == "inp") {
                    for (i in 9 downTo 1) {
                        val new = state.first.copyOf()
                        new.inp(command[1], i)
                        if (toAdd.none { it.first.contentEquals(new) }) {
                            toAdd.add(new to state.second.concat(i))
                        }
                    }
                } else {
                    val new = state.first.copyOf()
                    when (command.first()) {
                        "add" -> new.add(command[1], command[2])
                        "mul" -> new.mul(command[1], command[2])
                        "div" -> new.div(command[1], command[2])
                        "mod" -> new.mod(command[1], command[2])
                        "eql" -> new.eql(command[1], command[2])
                    }
                    if (toAdd.none { it.first.contentEquals(new) }) {
                        toAdd.add(new to state.second)
                    }
                }
            }
            states = toAdd
            println("iteration ${(iterator + 1).toString().padStart(3)} of ${commands.size}: added ${toAdd.size.toString().padStart(6)} new states")
        }
        return states.filter { it.first[3] == 0 }.maxOf { it.second ?: 0 }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    // val testInput = readInput("Day24_test")
    // check(part1(testInput) == 15)
    // check(part2(testInput) == 1134)

    val input = readInput("Day24")
    var result: Any

    // println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result: $result, expected: ")
    // println("Test 2 solved in ${measureTimeMillis { result = part2(testInput) }}ms with result: $result, expected: ")
    println("Part 1 solved in ${measureTimeMillis { result = part1alternate(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}

fun AluState.nextAluState(nextDigit: Int, commands: List<List<String>>) {
    for (command in commands){
        when (command.first()) {
            "inp" -> inp(command[1], nextDigit)
            "add" -> add(command[1], command[2])
            "mul" -> mul(command[1], command[2])
            "div" -> div(command[1], command[2])
            "mod" -> mod(command[1], command[2])
            "eql" -> eql(command[1], command[2])
        }
    }
}

fun AluState.inp(a: String, value: Int) {
    this[a.asVariable()] = value
}

fun AluState.add(a: String, b: String) {
    this[a.asVariable()] += b.toIntOrNull() ?: this[b.asVariable()]
}

fun AluState.mul(a: String, b: String) {
    this[a.asVariable()] *= b.toIntOrNull() ?: this[b.asVariable()]
}

fun AluState.div(a: String, b: String) {
    this[a.asVariable()] /= b.toIntOrNull() ?: this[b.asVariable()]
}

fun AluState.mod(a: String, b: String) {
    this[a.asVariable()] %= b.toIntOrNull() ?: this[b.asVariable()]
}

fun AluState.eql(a: String, b: String) {
    this[a.asVariable()] = if (this[a.asVariable()] == (b.toIntOrNull() ?: this[b.asVariable()])) 1 else 0
}

fun String.asVariable(): Int = when (this) {
    "w" -> 0
    "x" -> 1
    "y" -> 2
    "z" -> 3
    else -> error("Can only convert w, x, y, z to correct variable")
}

typealias AluState = IntArray
