package com.aplications.parcial2.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aplications.parcial2.data.model.Place
import com.aplications.parcial2.data.model.Trip

@Composable
fun DetalleViaje(
    navController: NavController,
    viaje: Trip,
    lugares: List<Place>
) {
    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("agregar/${viaje.id}") },
                containerColor = Color.Blue,
                contentColor = Color.White
            ) {
                Text("+", fontSize = 30.sp)
            }
        }
    ) { padding ->
        BodyDetalleViaje(
            modifier = Modifier.padding(padding),
            navController = navController,
            viaje = viaje,
            lugares = lugares
        )
    }
}

@Composable
fun BodyDetalleViaje(
    modifier: Modifier = Modifier,
    navController: NavController,
    viaje: Trip,
    lugares: List<Place>
) {
    val lugaresViaje = lugares.filter { it.tripId == viaje.id }

    Column(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("←", color = Color.White, fontSize = 24.sp)
            }
            Text(viaje.name, fontSize = 24.sp, color = Color.White)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(
                        text = viaje.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(viaje.destination, color = Color.Black)
                    Text("${viaje.startDate} - ${viaje.endDate}", color = Color.Gray)
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Lugares a visitar (${lugaresViaje.size})",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(15.dp))

            if (lugaresViaje.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay lugares todavía", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(lugaresViaje) { lugar ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(15.dp)) {
                                Text(
                                    text = lugar.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(lugar.city, color = Color.Black)
                                Text(lugar.tentativeDate, color = Color.Gray)
                                Text(lugar.description, color = Color.Black)
                                Text(lugar.category?.name ?: "", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetalleViaje() {
    DetalleViaje(
        navController = rememberNavController(),
        viaje = Trip(
            id = "1",
            name = "Playa",
            destination = "Cartagena",
            startDate = "2026-12-01",
            endDate = "2026-12-10"
        ),
        lugares = emptyList()
    )
}