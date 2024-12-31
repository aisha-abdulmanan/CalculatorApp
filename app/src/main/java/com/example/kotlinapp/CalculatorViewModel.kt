package com.example.kotlinapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*


// Sealed class for button input
sealed class CalculatorInput {
    data class Number(val value: Int) : CalculatorInput()
    data class Operator(val symbol: String) : CalculatorInput()
    data class UnaryOperator(val symbol: String) : CalculatorInput()
    data class Parenthesis(val symbol: String) : CalculatorInput()
    object Decimal : CalculatorInput()
    object Delete : CalculatorInput()
    object AllClear : CalculatorInput()
    object Equals : CalculatorInput()
    object PreviousAnswer: CalculatorInput()
}

class CalculatorViewModel : ViewModel() {
    private val _equationText = MutableLiveData(INITIAL_RESULT)
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData(INITIAL_RESULT)
    val resultText: LiveData<String> = _resultText

    // Companion object & Type inference
    companion object {
        const val INITIAL_RESULT = ""                // Type inference
        val NUMBERS = ('0'..'9').toList()            // Type inference
        val OPERATORS = listOf('+', '-', '*', '/')   // Type inference
        private var prevAns = INITIAL_RESULT         // Store the previous answer
        private var resultDisplayed = false          // Track whether result was displayed
    }

    // Function to handle button clicks
    fun onButtonClick(input: CalculatorInput) {
        val lastChar = _equationText.value?.lastOrNull()   // uses Null safety

        if (resultDisplayed) {                   // If "=" button was previously clicked
            if (input == CalculatorInput.Equals) {  // and if "=" button was clicked again
                return                              // don't make any changes
            } else {                                // or if button other than "=" is clicked
            _resultText.value = INITIAL_RESULT      // clear the result and
            resultDisplayed = false                 // update the var "resultDisplayed"
            }
        }

        when (input) {
            is CalculatorInput.Delete -> deleteChar()
            is CalculatorInput.AllClear -> clearAll()
            is CalculatorInput.Number -> handleNumberInput(input.value.toString(), lastChar)
            is CalculatorInput.Decimal -> addDecimal(lastChar)
            is CalculatorInput.Operator -> addOperator(input.symbol, lastChar)
            is CalculatorInput.UnaryOperator -> addUnaryOp(input.symbol, lastChar)
            is CalculatorInput.Parenthesis -> addParenthesis(input.symbol, lastChar)
            is CalculatorInput.Equals -> evaluateExpression()
            is CalculatorInput.PreviousAnswer -> insertPreviousAnswer()
        }
    }

    private fun deleteChar() {
        // Remove the last character if equation is not null
        _equationText.value = _equationText.value?.dropLast(1) ?: ""

        if (_resultText.value == "ERROR") {
            _resultText.value = INITIAL_RESULT
            resultDisplayed = false
        }
    }
    private fun clearAll() {
        _equationText.value = ""
        _resultText.value = INITIAL_RESULT
        resultDisplayed = false
    }
    private fun addParenthesis(symbol: String, lastChar: Char?) {
        // Open parenthesis
        if (symbol == "(") {
            if (lastChar !in NUMBERS && lastChar !in listOf(')','!', '%')) {
                _equationText.value = (_equationText.value ?: "") + symbol
            }
        }
        // Close parenthesis
        if (symbol == ")") {
            if (lastChar in NUMBERS || lastChar in listOf(')','!', '%')) {
                _equationText.value = (_equationText.value ?: "") + symbol
            }
        }
    }
    private fun handleNumberInput(number: String, lastChar: Char?) {
        if (lastChar != null) {
            if (lastChar in listOf(')', '!', '%')) {
                return
            }
            if (lastChar == '0' && isLastOperandZero(_equationText.value.orEmpty())) {
                _equationText.value = _equationText.value?.dropLast(1) + number
                return
            }
        }
        _equationText.value = (_equationText.value ?: "") + number
    }
    private fun addDecimal(lastChar: Char?) {
        if (lastChar != null && lastChar in NUMBERS) {
            _equationText.value = (_equationText.value ?: "") + "."
        }
    }
    private fun addOperator(symbol: String, lastChar: Char?) {
        // Avoid consecutive operators
        if (lastChar != null && lastChar in OPERATORS) {
            _equationText.value = _equationText.value?.dropLast(1) + symbol
            return
        }
        if (lastChar != null && (lastChar in NUMBERS || lastChar in listOf(')', '!', '%'))) {
            if (symbol != "^") {
                _equationText.value = (_equationText.value ?: "") + symbol
            }
            if (symbol == "^" && lastChar !in listOf('!', '%')) {
                _equationText.value = (_equationText.value ?: "") + symbol
            }
        }
    }
    private fun addUnaryOp(symbol: String, lastChar: Char?) {
        if (symbol == "√") {
            if (lastChar == null || lastChar in OPERATORS || lastChar == '(') {
                _equationText.value = (_equationText.value ?: "") + symbol
            }
            return
        }
        // symbol is either ! or %
        if (lastChar != null && (lastChar in NUMBERS || lastChar == ')')) {
            _equationText.value = (_equationText.value ?: "") + symbol
        }
    }
    private fun insertPreviousAnswer() {
        if (prevAns != "ERROR") {
            _equationText.value = (_equationText.value ?: "") + prevAns
        }
    }
    private fun evaluateExpression() {
        // Launching coroutine to evaluate the expression
        val parser = ExpressionParser()
        evaluateExpressionInBackground(parser) {
            // This block will be executed after the result is available
            prevAns = _resultText.value.toString()  // Save the result as the previous answer
            resultDisplayed = true  // Mark that the result has been displayed
        }
    }

    // Helper function to launch a coroutine for evaluating the expression
    @Suppress("Unused")
    fun evaluateExpressionInBackground(parser: Parsable, onResult: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val equation = _equationText.value ?: "" // _equationText.value.orEmpty()
                // Perform parsing and calculation on a background thread
                val result: Double = equation.calculateWith(parser)

                // Switch back to the main thread for UI updates
                withContext(Dispatchers.Main) {
                    // Update the result on the main thread
                    _resultText.value = result.formatResult()  // format result to <string>
                    // Call the callback once the result is available
                    onResult()
                }
            } catch (e: Exception) { // You can catch the exception and leave it unused
                // Handle any errors and show error message
                withContext(Dispatchers.Main) {
                    _resultText.value = "ERROR"
                }
            }
        }
    }

    // Helper function to map pressed button to input
    fun mapButtonToInput(btn: String): CalculatorInput {
        return when (btn) {
            "C" -> CalculatorInput.Delete
            "AC" -> CalculatorInput.AllClear
            "=" -> CalculatorInput.Equals
            "Ans" -> CalculatorInput.PreviousAnswer
            "." -> CalculatorInput.Decimal
            "√",  "!", "%" -> CalculatorInput.UnaryOperator(btn)
            "(", ")" -> CalculatorInput.Parenthesis(btn)
            "+", "-", "*", "/", "^" -> CalculatorInput.Operator(btn)
            else -> CalculatorInput.Number(btn.toIntOrNull() ?: 0)
        }
    }
}


// Checking if last operand is zero
fun isLastOperandZero(equation: String) : Boolean {
    val symbols = listOf('+', '-', '*', '/', '^', '√', '(',')','!','%','.')
    var counter = 0
    var operand = ""

    // Reverse iterate through the equation string
    for (index in equation.length - 1 downTo 0) {
        if (equation[index] in symbols) {
            break
        }
        operand += equation[index]
        counter++

        if (counter > 1) {
            return false
        }
    }
    // Return true if the condition is true
    return (counter == 1 && operand[0] == '0')
}

// Extension function for class Double to format the result to String
fun Double.formatResult(): String {
    return if (this % 1 == 0.0) {
        this.toInt().toString() // Smartcast to Int if result ends with .0
    } else {
        this.toString() // Keep as Double if it has a decimal part
    }
}
