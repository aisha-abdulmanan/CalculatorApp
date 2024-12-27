package com.example.kotlinapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.Calculator
import com.example.kotlinapp.R


@Preview(showBackground = true)
@Composable
fun CalculatorUI_Preview(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp),
    ){
        // Display card (top element)
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CardContentS()
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Display calculator screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray),
            contentAlignment = Alignment.BottomEnd
        ) {
            CalculatorScreenContentS()
        }

        //Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(10.dp))

        // Buttons
        val buttonList = listOf(
            "!", "(", ")", "AC", "C",
            "7", "8", "9", "^","√",
            "4", "5", "6", "*", "/",
            "1", "2", "3", "+", "-",
            "0", ".", "%", "Ans", "="
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
        ) {
            items(buttonList){
                CalculatorButtonS(btn = it, onClick = {
                    //onButtonClicked(it)
                    println("Button is Clicked")
                })
            }
        }
    }
}


@Composable
fun CalculatorButtonS(btn : String, onClick : ()-> Unit){
    Box(modifier = Modifier.padding(4.dp)) {
        FloatingActionButton(
            onClick = { },
            modifier = Modifier.size(65.dp),
            containerColor = getColorS(btn)
        ) {
            Text(
                text = btn,
                fontSize = if (btn != "Ans") 40.sp else 28.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}

fun getColorS(btn : String): Color{
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

@Composable
fun CardContentS(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_calculate_24),
            contentDescription = "Button with Vector Asset",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.width(5.dp))

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
fun CalculatorScreenContentS() {
    Column (
        horizontalAlignment = Alignment.End
    ){
        // showing Equation
        Text(
            text = "123 + 123",
            fontSize = 45.sp,
            textAlign = TextAlign.End,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(8.dp)
        )
        // showing Result
        Text(
            text = " = 246",
            fontSize = 55.sp,
            color = Color(0xFF7F4CA5),
            textAlign = TextAlign.End,
            modifier = Modifier.padding(8.dp)
        )
    }
}