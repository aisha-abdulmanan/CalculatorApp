package com.example.kotlinapp

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CalculatorUI(viewModel: CalculatorViewModel){
    val equationText = viewModel.equationText.observeAsState()
    val resultText = viewModel.resultText.observeAsState()
    fun onButtonClicked(btn: String) {
        val input = viewModel.mapButtonToInput(btn)
        viewModel.onButtonClick(input)
    }

    // Screen
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        // Display card (top element)
        Card(modifier = Modifier.fillMaxWidth()) { CardContent() }

        // Display calculator screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray),
            contentAlignment = Alignment.BottomEnd
        ) { CalculatorScreenContent(equationText.value?: "", resultText.value?: "") }

        // Display buttons
        val buttonList = listOf(
            "!", "(", ")", "AC", "C",
            "7", "8", "9", "^","√",
            "4", "5", "6", "*", "/",
            "1", "2", "3", "+", "-",
            "0", ".", "%", "Ans", "="
        )
        LazyVerticalGrid(columns = GridCells.Fixed(5)) {
            items(buttonList){
                CalculatorButton(btn = it, onClick = {
                    onButtonClicked(it)
                })
            }
        }
    }
}


@Composable
fun CardContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_calculate_24),
            contentDescription = "Button with Vector Asset",
            modifier = Modifier.size(80.dp)
        )

        Text(
            text = "Calculator App",
            color = Color.Black,
            fontSize = 35.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun CalculatorScreenContent(equationText: String, resultText: String) {
    Column (
        horizontalAlignment = Alignment.End
    ){
        // Display Equation text
        Text(
            text = equationText,
            fontSize = 45.sp,
            textAlign = TextAlign.End,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(8.dp)
        )
        // Display Result text
        Text(
            text = resultText,
            fontSize = 55.sp,
            color = Color(0xFF7F4CA5),
            textAlign = TextAlign.End,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun CalculatorButton(btn : String, onClick : ()-> Unit){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    Box(modifier = Modifier.padding(3.dp)) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = getColor(btn),
            modifier = Modifier.size(65.dp),
            interactionSource = interactionSource

        ) {
            Text(
                text = btn,
                fontSize = if (btn != "Ans") 40.sp else 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (isPressed.value) { //
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.DarkGray.copy(alpha = 0.6f)) // Semi-transparent overlay
            )
        }
    }
}

fun getColor(btn : String): Color{
    val operators = listOf("/", "*", "-", "+", "^")
    val specialOperators = listOf("√", "!", "%")
    val parenthesis = listOf("(", ")")

    if(btn == "C" || btn == "AC")
        return Color(0xFFF54E43)
    if(btn == "=")
        return Color(0xFFBB6CEA)
    if(btn in operators || btn in specialOperators)
        return Color(0xFFFF9800)
    if(btn == "Ans")
        return Color(0xFFFF006F)
    if(btn in parenthesis)
        return Color(0xFF909898)

    return Color(0xFF00C8C9)
}
