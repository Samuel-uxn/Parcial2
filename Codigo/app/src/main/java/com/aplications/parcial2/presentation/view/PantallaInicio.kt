package com.aplications.parcial2.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.navigation.compose.rememberNavController
import com.aplications.parcial2.AppContainer
import com.aplications.parcial2.data.model.Trip
import com.aplications.parcial2.presentation.viewmodel.TripViewModel

/** Pantalla: obtiene el estado del ViewModel (datos reales del backend). */
@Composable
fun PantallaInicio(
    navController: NavController,
    viewModel: TripViewModel = viewModel(
        factory = viewModelFactory { initializer { TripViewModel(AppContainer.repository) } }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Recarga cada vez que se vuelve a esta pantalla (por ejemplo, tras crear un viaje)
    LaunchedEffect(Unit) { viewModel.loadTrips() }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("crear") },
                containerColor = Color.Blue,
                contentColor = Color.White
            ) {
                Text("+", fontSize = 30.sp)
            }
        }
    ) { padding ->
        BodyInicio(
            modifier = Modifier.padding(padding),
            navController = navController,
            viajes = state.trips,
            cargando = state.isLoading,
            error = state.error,
            onReintentar = viewModel::loadTrips
        )
    }
}

/** Solo dibuja: recibe datos y funciones, no conoce el ViewModel (se puede previsualizar). */
@Composable
fun BodyInicio(
    modifier: Modifier = Modifier,
    navController: NavController,
    viajes: List<Trip>,
    cargando: Boolean = false,
    error: String? = null,
    onReintentar: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize()) {

        Text(
            text = "Mis viajes",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .padding(20.dp)
        )

        if (error != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = error, color = Color.Red)
                Spacer(Modifier.height(10.dp))
                Button(onClick = onReintentar) { Text("Reintentar") }
            }
        } else if (cargando && viajes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (viajes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No hay viajes todavía", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 15.dp,
                    top = 15.dp,
                    end = 15.dp,
                    bottom = 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(viajes, key = { it.id }) { viaje ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("detalle/${viaje.id}")
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Text(
                                text = viaje.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = viaje.destination,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaInicio() {
    BodyInicio(
        navController = rememberNavController(),
        viajes = listOf(
            Trip("1", "Europa 2026", "Italia", "2026-06-10", "2026-06-20"),
            Trip("2", "Playa", "Cartagena", "2026-12-01", "2026-12-10")
        )
    )
}