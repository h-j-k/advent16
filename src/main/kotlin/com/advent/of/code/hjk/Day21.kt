package com.advent.of.code.hjk

object Day21 {

    fun part1(input: String, rawOperations: List<String>): String = StringBuilder(input).also { r ->
        rawOperations.map { op -> op to operations.single { it.regex.matches(op) } }
            .forEach { (k, op) -> op(op.result(k), r) }
    }.toString()

    private val operations =
        setOf(SwapPosition, SwapLetter, RotateLeftRight, RotateByPosition, ReversePosition, MovePosition)

    private sealed interface Operation : (MatchResult, StringBuilder) -> Unit {
        val regex: Regex

        val MatchResult.toInts: List<Int> get() = destructured.toList().map { it.toInt() }

        val MatchResult.toChars: List<Char> get() = destructured.toList().map { it[0] }

        fun result(input: String) = regex.matchEntire(input)!!
    }

    private object SwapPosition : Operation {
        override val regex = "swap position (?<x>\\d) with position (?<y>\\d)".toRegex()

        override fun invoke(result: MatchResult, input: StringBuilder) {
            val (x, y) = result.toInts
            val t = input[x]
            input[x] = input[y]
            input[y] = t
        }
    }

    private object SwapLetter : Operation {
        override val regex = "swap letter (?<x>.) with letter (?<y>.)".toRegex()

        override fun invoke(result: MatchResult, input: StringBuilder) {
            val (x, y) = result.toChars
            input.indices.forEach { if (input[it] == x) input[it] = y else if (input[it] == y) input[it] = x }
        }
    }

    private object RotateLeftRight : Operation {
        override val regex = "rotate (?<direction>left|right) (?<x>\\d) steps?".toRegex()

        override fun invoke(result: MatchResult, input: StringBuilder) {
            val (direction, x) = result.destructured
            val offset = x.toInt() % input.length
            val temp = input.toString()
            input.clear()
            if (direction == "left") input.append(temp.drop(offset) + temp.take(offset))
            else input.append(temp.takeLast(offset) + temp.dropLast(offset))
        }
    }

    private object RotateByPosition : Operation {
        override val regex = "rotate based on position of letter (?<x>.)".toRegex()

        override fun invoke(result: MatchResult, input: StringBuilder) {
            val (x) = result.toChars
            val steps = input.indexOf(x).let { if (it < 4) it + 1 else it + 2 }
            RotateLeftRight(RotateLeftRight.result("rotate right $steps steps"), input)
        }
    }

    private object ReversePosition : Operation {
        override val regex = "reverse positions (?<x>\\d) through (?<y>\\d)".toRegex()

        override fun invoke(result: MatchResult, input: StringBuilder) {
            val (x, y) = result.toInts
            val range = x..(y + 1)
            input.setRange(range.first, range.last, input.subSequence(range.first, range.last).reversed().toString())
        }
    }

    private object MovePosition : Operation {
        override val regex = "move position (?<x>\\d) to position (?<y>\\d)".toRegex()

        override fun invoke(result: MatchResult, input: StringBuilder) {
            val (x, y) = result.toInts
            if (x == y) return
            val c = input[x]
            input.replace(x, x + 1, "")
            if (x < y) input.replace(y - 1, y, "${input[y - 1]}$c")
            else if (x > y) input.replace(y, y + 1, "$c${input[y]}")
        }
    }
}