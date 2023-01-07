package com.example.stm_client

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stm_client.ui.theme.STMClientTheme
import java.io.ByteArrayInputStream
import java.io.InputStream
import kotlin.math.abs


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
                    MapViewer()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    STMClientTheme {
        MapViewer()
    }
}

const val minLat = 54.333251000649454
const val maxLat = 54.413205389562194
const val minLng = 18.572490218700082
const val maxLng = 18.709215200253727
var selectedLatMin = minLat
var selectedLngMin = minLng
var selectedLatMax = maxLat
var selectedLngMax = maxLng


@Composable
fun MapViewer()
{
    var imageBase64 by remember { mutableStateOf("R0lGODlhAQABAIAAAMLCwgAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==") }
    var latMinText by remember { mutableStateOf(minLat.toString().substring(0,10)) }
    var latMaxText by remember { mutableStateOf(maxLat.toString().substring(0,10)) }
    var longMinText by remember { mutableStateOf(minLng.toString().substring(0,10)) }
    var longMaxText by remember { mutableStateOf(maxLng.toString().substring(0,10)) }

    val decodedString = Base64.decode(imageBase64, Base64.DEFAULT)
    val inputStream: InputStream = ByteArrayInputStream(decodedString)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    Column(
        Modifier
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)){

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "contentDescription",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .height(200.dp)
                .width(200.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = latMaxText,
            onValueChange = {
                if(it != null && it != "")
                {
                    var castValue = latMaxText.toDouble();
                    if(castValue < selectedLatMin)
                    {
                        castValue = selectedLatMin + 0.001;
                    }
                    if(castValue > maxLat)
                    {
                        castValue = maxLat;
                    }
                    latMaxText = castValue.toString()
                    selectedLatMax = castValue
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .width(150.dp),
            label = { Text(text = "max lat");
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)) {
            TextField(
                value = longMinText,
                onValueChange = {
                    if(it != null && it != "")
                    {
                        var castValue = latMaxText.toDouble();
                        if(castValue > selectedLngMax)
                        {
                            castValue = selectedLngMax - 0.001;
                        }
                        if(castValue < minLng)
                        {
                            castValue = minLng;
                        }
                        latMaxText = castValue.toString()
                        selectedLngMin = castValue
                    }

                },
                modifier = Modifier.width(150.dp),
                label = { Text(text = "min long");
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(25.dp))
            TextField(
                value = longMaxText,
                onValueChange = {
                    if(it != null && it != "")
                    {
                        var castValue = latMaxText.toDouble();
                        if(castValue < selectedLngMin)
                        {
                            castValue = selectedLngMin + 0.001;
                        }
                        if(castValue > maxLng)
                        {
                            castValue = maxLng;
                        }
                        latMaxText = castValue.toString()
                        selectedLngMax = castValue
                    }
                },
                modifier = Modifier.width(150.dp),
                label = { Text(text = "max long");
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        TextField(
            value = latMinText,
            onValueChange = {
                if(it != null && it != "")
                {
                    var castValue = latMaxText.toDouble();
                    if(castValue > selectedLatMax)
                    {
                        castValue = selectedLatMax - 0.001;
                    }
                    if(castValue < minLat)
                    {
                        castValue = minLat;
                    }
                    latMaxText = castValue.toString()
                    selectedLatMin = castValue
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .width(150.dp),
            label = { Text(text = "min lat");
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally))
        {
            Button(onClick = {
                selectedLatMin = minLat
                selectedLatMax = maxLat
                selectedLngMin = minLng
                selectedLngMax = maxLng
                latMinText = minLat.toString().substring(0,10)
                latMaxText = maxLat.toString().substring(0,10)
                longMinText = minLng.toString().substring(0,10)
                longMaxText = maxLng.toString().substring(0,10)
            }) { Text("Set Max") }

            Spacer(Modifier.width(25.dp))

            Button(onClick = {
                var mapRequestHandler = MapRequestHandler()
                var listener = IMapResponseListener { imageBase64 = it }
                mapRequestHandler.SendRequest(listener, selectedLatMin, selectedLatMax, selectedLngMin, selectedLngMax)
            }){ Text("Evaluate") }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally))
        {
            Button(onClick = {
                selectedLatMin += 0.0039
                selectedLatMax -= 0.0039
                selectedLngMin += 0.0068
                selectedLngMax -= 0.0068

                if(selectedLatMin < minLat) selectedLatMin = minLat
                if(selectedLatMax > maxLat) selectedLatMax = maxLat
                if(selectedLngMin < minLng) selectedLngMin = minLng
                if(selectedLngMax > maxLng) selectedLngMax = maxLng

                if(selectedLatMin > selectedLatMax) selectedLatMin = selectedLatMax - 0.001
                if(selectedLatMax < selectedLatMin) selectedLatMax = selectedLatMin + 0.001
                if(selectedLngMin > selectedLngMax) selectedLngMin = selectedLngMax - 0.001
                if(selectedLngMax < selectedLngMin) selectedLngMax = selectedLngMin + 0.001

                latMinText = selectedLatMin.toString().substring(0,10)
                latMaxText = selectedLatMax.toString().substring(0,10)
                longMinText = selectedLngMin.toString().substring(0,10)
                longMaxText = selectedLngMax.toString().substring(0,10)
            }) { Text("Shrink") }

            Spacer(Modifier.width(25.dp))

            Button(onClick = {
                selectedLatMin -= 0.0039
                selectedLatMax += 0.0039
                selectedLngMin -= 0.0068
                selectedLngMax += 0.0068

                if(selectedLatMin < minLat) selectedLatMin = minLat
                if(selectedLatMax > maxLat) selectedLatMax = maxLat
                if(selectedLngMin < minLng) selectedLngMin = minLng
                if(selectedLngMax > maxLng) selectedLngMax = maxLng

                if(selectedLatMin > selectedLatMax) selectedLatMin = selectedLatMax - 0.001
                if(selectedLatMax < selectedLatMin) selectedLatMax = selectedLatMin + 0.001
                if(selectedLngMin > selectedLngMax) selectedLngMin = selectedLngMax - 0.001
                if(selectedLngMax < selectedLngMin) selectedLngMax = selectedLngMin + 0.001

                latMinText = selectedLatMin.toString().substring(0,10)
                latMaxText = selectedLatMax.toString().substring(0,10)
                longMinText = selectedLngMin.toString().substring(0,10)
                longMaxText = selectedLngMax.toString().substring(0,10)
            }){ Text("Expand") }
        }


        Spacer(modifier = Modifier.height(25.dp))

        Text("Input info:",
            Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
        Text("LAT: ${minLat.toString().substring(0,5)} - ${maxLat.toString().substring(0,5)}",
            Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
        Text("LNGS: ${minLng.toString().substring(0,5)} - ${maxLng.toString().substring(0,5)}",
            Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))
    }

}

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
                println(selectedLatMin);
                println(selectedLngMin);
                println(selectedLatMax);
                println(selectedLngMax);
                var mapRequestHandler =
                    MapRequestHandler()
                var listener = IMapResponseListener {
                    inputImageBitMap = it
                };

                if(abs(selectedLatMin) <= 0.1) selectedLngMin = minLat;
                if(abs(selectedLatMax) <= 0.1) selectedLatMax = maxLat;
                if(abs(selectedLngMin) <= 0.1) selectedLngMin = minLng;
                if(abs(selectedLngMax) <= 0.1) selectedLngMax = maxLng;

                selectedLatMin = Math.min(selectedLatMin, maxLat);
                selectedLatMin = Math.max(selectedLatMin, minLat);

                selectedLatMax = Math.min(selectedLatMax, maxLat);
                selectedLatMax = Math.max(selectedLatMax, minLat);

                selectedLngMin = Math.min(selectedLngMin, maxLng);
                selectedLngMin = Math.max(selectedLngMin, minLng);

                selectedLngMax = Math.min(selectedLngMax, maxLng);
                selectedLngMax = Math.max(selectedLngMax, minLng);

                mapRequestHandler.SendRequest(listener, selectedLngMin, selectedLatMin, selectedLngMax, selectedLatMax)

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

    var inputLat = ""
    var inputLng = ""
    if(id==0){
        inputLat = minLat.toString().substring(0,5)
        inputLng = minLng.toString().substring(0,5)
    }else{
        inputLat = maxLat.toString().substring(0,5)
        inputLng = maxLng.toString().substring(0,5)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally))
    {
        Text(title)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Lat", modifier = Modifier.width(30.dp))
            Spacer(modifier = Modifier.width(20.dp

            ))
            TextField(
                value = latText,
                onValueChange = { latText = it
                    if(id==0){
                        selectedLngMin = latText.toDouble();
                    }else{
                        selectedLngMax = latText.toDouble();
                    }

                },
                modifier = Modifier,

                label = { Text(text = inputLat)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Lng", modifier = Modifier.width(30.dp))
            Spacer(modifier = Modifier.width(20.dp

            ))
            TextField(
                value = longText,
                onValueChange = { longText = it
                    if(id==0){
                        selectedLatMin = longText.toDouble()
                    }else{
                        selectedLatMax = longText.toDouble()
                    }
                },
                modifier = Modifier,
                label = { Text(text = inputLng) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }


    }
}
