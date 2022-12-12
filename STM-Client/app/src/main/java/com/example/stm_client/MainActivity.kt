package com.example.stm_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stm_client.ui.theme.STMClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            STMClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MapDisplay()
                }
            }
        }
    }
}

const val minLat = 54.333251000649454
const val maxLat = 54.413205389562194
const val minLong = 18.572490218700082
const val maxLong = 18.709215200253727

@Composable
fun MapDisplay()
{
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        Column{
            DisplayMap(minLat, minLong, maxLat, maxLong)

            Column(modifier = Modifier.padding(10.dp)){
                CoordinatesInput("Bottom Left")
                Spacer(modifier = Modifier.height(10.dp))
                CoordinatesInput("Top Right")
            }

            Button(onClick = {

            }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)){
                Text("Evaluate")
            }
        }
    }
}

@Composable
fun DisplayMap(minLat: Double, minLong: Double, maxLat: Double, maxLong: Double)
{
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally)
        .width(300.dp)
        .height(300.dp)
        .background(Color.DarkGray)){

    }
}

@Composable
fun CoordinatesInput(title: String) {
    var longText by remember { mutableStateOf("") }
    var latText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally))
    {
        Text(title)
        TextField(
            value = longText,
            onValueChange = { longText = it },
            modifier = Modifier,
            label = { Text(text = "long") } )
        TextField(
            value = latText,
            onValueChange = { latText = it },
            modifier = Modifier,
            label = { Text(text = "lat") } )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    STMClientTheme {
        MapDisplay()
    }
}