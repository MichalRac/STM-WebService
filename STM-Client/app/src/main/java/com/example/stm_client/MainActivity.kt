package com.example.stm_client

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stm_client.ui.theme.STMClientTheme
import java.io.ByteArrayInputStream
import java.io.InputStream


class MainActivity : ComponentActivity() {
    val NAMESPACE = "http://tempuri.org/"; // com.service.ServiceImpl
    val URL = "http://commodities.karvy.com/services/NetPositionReport.asmx";
    val METHOD_NAME = "NetPositionReport";
    val SOAP_ACTION = "http://tempuri.org/NetPositionReport";
    var webResponse = "";
    var handler = Handler();
    var thread = Thread();




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
{   var inputImageBitMap by remember { mutableStateOf("\"iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII\"") }
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
                var testRequest = TestRequest()
                testRequest.startWebAccess("")
            }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)){
                Text("Evaluate")
            }
            FetchedImage(inputImageBitMap)
        }
    }
}

@Composable
fun DisplayMap(minLat: Double, minLong: Double, maxLat: Double, maxLong: Double)
{
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally)
        .width(200.dp)
        .height(200.dp)
        .background(Color.DarkGray)){

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

        }
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "contentDescription"
        )
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
            label = { Text(text = "long")

            } )
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