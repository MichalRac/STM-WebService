package com.example.stm_client

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stm_client.ui.theme.STMClientTheme
import java.io.ByteArrayInputStream
import java.io.InputStream


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

var fetchedImageBase64 = "";
const val minLat = 54.333251000649454
const val maxLat = 54.413205389562194
const val minLong = 18.572490218700082
const val maxLong = 18.709215200253727
var selectedLat1 = 0.0
var selectedLng1 = 0.0
var selectedLat2 =0.0
var selectedLng2 = 0.0

@Composable
fun MapDisplay()
{
    var inputImageBitMap by remember { mutableStateOf("iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII") }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        Column{
            FetchedImage(inputImageBitMap)

            Column(modifier = Modifier.padding(10.dp)){
                CoordinatesInput("Bottom Left", 0)
                Spacer(modifier = Modifier.height(10.dp))
                CoordinatesInput("Top Right", 1)
            }

            Button(onClick = {
                println(selectedLat1);
                println(selectedLng1);
                println(selectedLat2);
                println(selectedLng2);
                var testRequest = TestRequest()
                var listener = IMapProviderListener { inputImageBitMap = it };
                testRequest.startWebAccess(listener, selectedLng1, selectedLat1, selectedLng2, selectedLat2)

            }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)){
                Text("Evaluate")
            }
        }
    }
}

@Composable
fun FetchedImage(input:String){

    val decodedString = Base64.decode(input, Base64.DEFAULT)
    val inputStream: InputStream = ByteArrayInputStream(decodedString)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .width(200.dp)
            .height(200.dp),
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "contentDescription",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        }
    }


@Composable
fun CoordinatesInput(title: String, id:Int) {
    var longText by remember { mutableStateOf("") }
    var latText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally))
    {
        Text(title)
        TextField(
            value = longText,
            onValueChange = { longText = it
                if(id==0){
                    selectedLng1 = longText.toDouble();
                }else{
                    selectedLng2 = longText.toDouble();
                }

                            },
            modifier = Modifier,

            label = { Text(text = "long");
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = latText,
            onValueChange = { latText = it
                            if(id==0){
                                selectedLat1 = latText.toDouble()
                            }else{
                            selectedLat2 = latText.toDouble()
                            }
            },
            modifier = Modifier,
            label = { Text(text = "lat") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    STMClientTheme {
        MapDisplay()
    }
}