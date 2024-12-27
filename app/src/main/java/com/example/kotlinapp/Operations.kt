package com.example.kotlinapp

import kotlin.math.*

// Abstract class for mathematical operations
abstract class Operation {
    abstract fun evaluate(vararg operands: Double): Double
}

// Operations
class Add : Operation() {
    override fun evaluate(vararg operands: Double) = operands[0] + operands[1]
}
class Subtract : Operation() {
    override fun evaluate(vararg operands: Double) = operands[0] - operands[1]
}
class Multiply : Operation() {
    override fun evaluate(vararg operands: Double) = operands[0] * operands[1]
}
class Divide : Operation() {
    override fun evaluate(vararg operands: Double): Double {
        require(operands[1] != 0.0) { "Division by zero is undefined" }
        return operands[0] / operands[1]
    }
}
class Exponent : Operation() {
    override fun evaluate(vararg operands: Double) = operands[0].pow(operands[1])
}
class SquareRoot : Operation() {
    override fun evaluate(vararg operands: Double) = sqrt(operands[0])
}
class Factorial : Operation() {
    override fun evaluate(vararg operands: Double): Double {
        val number = operands[0]
        require(number >= 0) { "Factorial is undefined for negative numbers" }
        require(number % 1 == 0.0) { "Factorial is only defined for integers" }

        val n = number.toInt()
        return if (n == 0) 1.0 else (1..n).fold(1.0) { acc, i -> acc * i }
    }
}
class Percentage : Operation() {
    override fun evaluate(vararg operands: Double): Double {
        return operands[0] / 100
    }
}
