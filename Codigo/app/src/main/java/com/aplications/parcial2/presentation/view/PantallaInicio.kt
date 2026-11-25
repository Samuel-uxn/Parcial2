package com.aplications.parcial2.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaInicio()
{
    Scaffold(containerColor = Color.White) { padding ->
        BodyContent(Modifier.padding(padding))
    }
}

@Composable
fun BodyContent(padding: Modifier)
{
    Column(
        modifier = padding
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Mis Viajes",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))



        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "seleccione algun viaje o agregue uno nuevo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(15.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0))
                .clickable {}
                .padding(15.dp)
        ) {
            Column {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF008C59)
                    )
                ) {
                    Text("Cartagena")
                }
                Text(
                    text = "Colombia 2026",
                    fontWeight = FontWeight.Bold
                )


                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0))
                .clickable {}
                .padding(15.dp)
        ) {
            Column {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF008C59)
                    )
                ) {
                    Text("Italia")
                }
                Text(
                    text = "Europa 2027",
                    fontWeight = FontWeight.Bold
                )


                Spacer(modifier = Modifier.height(10.dp))
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview()
{
    PantallaInicio()
}