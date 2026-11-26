package com.aplications.parcial2.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.aplications.parcial2.AppContainer
import com.aplications.parcial2.data.model.Category
import com.aplications.parcial2.data.model.Place
import com.aplications.parcial2.data.model.TripDetail
import com.aplications.parcial2.presentation.viewmodel.TripDetailViewModel

@Composable
fun DetalleViaje(
    navController: NavController,
    tripId: String,
    viewModel: TripDetailViewModel = viewModel(
        factory = viewModelFactory { initializer { TripDetailViewModel(AppContainer.repository, tripId) } }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Recarga al volver (por ejemplo, después de agregar un lugar)
    LaunchedEffect(Unit) { viewModel.loadTrip() }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("agregar/$tripId") },
                containerColor = Color.Blue,
                contentColor = Color.White
            ) {
                Text("+", fontSize = 30.sp)
            }
        }
    ) { padding ->
        BodyDetalleViaje(
            modifier = Modifier.padding(padding),
            viaje = state.trip,
            cargando = state.isLoading,
            error = state.error,
            onReintentar = viewModel::loadTrip,
            onVolver = { navController.popBackStack() }
        )
    }
}

@Composable
fun BodyDetalleViaje(
    modifier: Modifier = Modifier,
    viaje: TripDetail?,
    cargando: Boolean = false,
    error: String? = null,
    onReintentar: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onVolver) {
                Text("←", color = Color.White, fontSize = 24.sp)
            }
            Text(viaje?.name ?: "Detalle del viaje", fontSize = 24.sp, color = Color.White)
        }

        if (error != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(error, color = Color.Red)
                Spacer(Modifier.height(10.dp))
                Button(onClick = onReintentar) { Text("Reintentar") }
            }
        } else if (viaje == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (cargando) CircularProgressIndicator()
            }
        } else {
            // Solo los lugares que pertenecen a este viaje
            val lugaresViaje = viaje.places.filter { it.tripId == viaje.id }

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
                        items(lugaresViaje, key = { it.id }) { lugar ->
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
}

@Preview(showBackground = true)
@Composable
fun PreviewDetalleViaje() {
    BodyDetalleViaje(
        viaje = TripDetail(
            id = "1",
            name = "Playa",
            destination = "Cartagena",
            startDate = "2026-12-01",
            endDate = "2026-12-10",
            places = listOf(
                Place(
                    id = "p1", tripId = "1", name = "Castillo San Felipe",
                    description = "Fortaleza histórica", city = "Cartagena",
                    categoryId = 1, tentativeDate = "2026-12-03",
                    category = Category(1, "Histórico", "")
                )
            ),
            placesCount = 1
        )
    )
}