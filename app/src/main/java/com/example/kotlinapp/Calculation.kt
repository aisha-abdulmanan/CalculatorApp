package com.example.kotlinapp

import java.util.Stack


// Calculator to evaluate tokens
class Calculator {
    fun evaluate(tokens: List<Token>): Double {
        val operatorStack = Stack<Token.Operator>()
        val operandStack = Stack<Double>()

        // Immutable lambda (val) to process operators and evaluate expressions dynamically
        val process = {
            val operator = operatorStack.pop()
            val operands = mutableListOf<Double>()
            val unaryOperator = listOf('√', '!', '%')

            repeat(if (operator.symbol in unaryOperator) 1 else 2) {
                operands.add(operandStack.pop())
            }

            operandStack.push(operator.operation.evaluate(*operands.reversed().toDoubleArray()))
        }

        tokens.forEach { token ->
            when (token) {
                is Token.Number -> operandStack.push(token.value)
                is Token.Operator -> {
                    while (operatorStack.isNotEmpty() && operatorStack.peek().precedence >= token.precedence) {
                        process()
                    }
                    operatorStack.push(token)
                }
                Token.LeftParenthesis -> operatorStack.push(Token.Operator('(', Int.MIN_VALUE, Add()))
                Token.RightParenthesis -> {
                    while (operatorStack.peek().symbol != '(') {
                        process()
                    }
                    operatorStack.pop() // Remove '('
                }
            }
        }

        while (operatorStack.isNotEmpty()) {
            process()
        }

        return operandStack.pop()
    }
}

// Infix function for parsing and calculating
infix fun String.calculateWith(parser: Parsable): Double {
    val tokens = parser.parse(this)
    return Calculator().evaluate(tokens)
}

