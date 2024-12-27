package com.example.kotlinapp

// Sealed class to represent Tokens
sealed class Token {
    data class Number(val value: Double) : Token()
    data class Operator(val symbol: Char, val precedence: Int, val operation: Operation) : Token()
    object LeftParenthesis : Token()
    object RightParenthesis : Token()
}

// Interface for parsing expressions
interface Parsable {
    fun parse(input: String): List<Token>
}

// Parser implementation
class ExpressionParser : Parsable {
    override fun parse(input: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var index = 0

        while (index < input.length) {
            val char = input[index]

            when {
                char.isDigit() || char == '.' -> {
                    val number = StringBuilder()
                    while (index < input.length && (input[index].isDigit() || input[index] == '.')) {
                        number.append(input[index])
                        index++
                    }
                    tokens.add(Token.Number(number.toString().toDouble()))
                    index--
                }
                char == '+' -> tokens.add(Token.Operator('+', 1, Add()))
                char == '-' -> tokens.add(Token.Operator('-', 1, Subtract()))
                char == '*' -> tokens.add(Token.Operator('*', 2, Multiply()))
                char == '/' -> tokens.add(Token.Operator('/', 2, Divide()))
                char == '^' -> tokens.add(Token.Operator('^', 3, Exponent()))
                char == '√' -> tokens.add(Token.Operator('√', 4, SquareRoot()))
                char == '%' -> tokens.add(Token.Operator('%', 4, Percentage()))
                char == '!' -> tokens.add(Token.Operator('!', 5, Factorial()))
                char == '(' -> tokens.add(Token.LeftParenthesis)
                char == ')' -> tokens.add(Token.RightParenthesis)
                else -> throw IllegalArgumentException("Invalid character: $char")
            }
            index++
        }
        return tokens
    }
}
