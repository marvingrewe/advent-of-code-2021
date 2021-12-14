import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val polymer = input[0].toMutableList()
        val rules = input.takeLastWhile(String::isNotBlank)
            .associate { with(it.split(" -> ")) { component1() to component2() } }

        repeat(10) { polymer.polymerize(rules) }
        val polymerCounts = polymer.groupingBy { it }.eachCount()
        return polymerCounts.maxOf { it.value } - polymerCounts.minOf { it.value }
    }

    fun part2(input: List<String>): Long {
        val rules = input.takeLastWhile(String::isNotBlank)
            .associate { with(it.split(" -> ")) { component1() to component2() } }
        val polymer = rules.keys.associateWith { 0L }.toMutableMap()
        for (pair in input[0].zipWithNext()) {
            val key = pair.first.toString() + pair.second
            polymer.compute(key) { _, v -> v?.plus(1) }
        }

        repeat(40) {
            polymer.polymerize((rules))
        }
        val components = mutableMapOf<Char, Long>()
        for ((key, value) in polymer) {
            components.putIfAbsent(key.first(), 0L)
            components.putIfAbsent(key.last(), 0L)
            components[key.first()] = components[key.first()]!! + value
            components[key.last()] = components[key.last()]!! + value
        }
        for ((key, value) in components) {
            components[key] = (value + 1) / 2
        }
        return components.maxOf { it.value } - components.minOf { it.value }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    //check(part1(testInput) == 15)
    //check(part2(testInput) == 1134)

    val input = readInput("Day14")
    var result: Any

    println("Test 1 solved in ${measureTimeMillis { result = part1(testInput) }}ms with result: $result, expected: 1588")
    println("Test 2 solved in ${measureTimeMillis { result = part2(testInput) }}ms with result: $result, expected: 2188189693529")
    println("Part 1 solved in ${measureTimeMillis { result = part1(input) }}ms with result: $result")
    println("Part 2 solved in ${measureTimeMillis { result = part2(input) }}ms with result: $result")
}

fun MutableList<Char>.polymerize(rules: Map<String, String>) {
    val insertion = mutableListOf<String>()
    this.zipWithNext().map { pair -> rules[pair.first.toString() + pair.second]?.let { it1 -> insertion.add(it1) } }
    //println(insertion)
    for ((position, value) in insertion.withIndex()) {
        this.add(position * 2 + 1, value.first())
    }
}

fun MutableMap<String, Long>.polymerize(rules: Map<String, String>) {
    val temp = this.toMutableMap()
    for ((key, value) in temp) {
        val leftHalf = key.take(1) + rules[key]
        val rightHalf = rules[key] + key.last()
        // break up left part of assignment rule
        this.computeIfPresent(key) { _, v -> v - value }
        this.computeIfPresent(leftHalf) { _, v -> v + value }
        this.computeIfPresent(rightHalf) { _, v -> v + value }
    }
}
