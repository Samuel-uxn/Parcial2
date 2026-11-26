package com.aplications.parcial2.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aplications.parcial2.data.model.Trip
import java.util.UUID

@Composable
fun CrearViaje(
    navController: NavController,
    viajes: MutableList<Trip>
) {
    Scaffold { padding ->
        BodyCrearViaje(
            modifier = Modifier.padding(padding),
            navController = navController,
            viajes = viajes
        )
    }
}

@Composable
fun BodyCrearViaje(
    modifier: Modifier = Modifier,
    navController: NavController,
    viajes: MutableList<Trip>
) {
    var nombre by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {

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
            Text("Crear viaje", fontSize = 24.sp, color = Color.White)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del viaje") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(15.dp))

            OutlinedTextField(
                value = destino,
                onValueChange = { destino = it },
                label = { Text("Destino") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(15.dp))

            Row {
                OutlinedTextField(
                    value = fechaInicio,
                    onValueChange = { fechaInicio = it },
                    label = { Text("Fecha inicio") },
                    placeholder = { Text("AAAA-MM-DD") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(10.dp))

                OutlinedTextField(
                    value = fechaFin,
                    onValueChange = { fechaFin = it },
                    label = { Text("Fecha fin") },
                    placeholder = { Text("AAAA-MM-DD") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            if (mensaje.isNotEmpty()) {
                Spacer(Modifier.height(15.dp))
                Text(mensaje, color = Color.Red)
            }
        }

        Button(
            onClick = {
                if (nombre.isBlank() || destino.isBlank() ||
                    fechaInicio.isBlank() || fechaFin.isBlank()
                ) {
                    mensaje = "Complete todos los campos"
                } else {
                    viajes.add(
                        Trip(
                            id = UUID.randomUUID().toString(),
                            name = nombre.trim(),
                            destination = destino.trim(),
                            startDate = fechaInicio.trim(),
                            endDate = fechaFin.trim()
                        )
                    )
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Guardar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCrearViaje() {
    CrearViaje(
        navController = rememberNavController(),
        viajes = mutableListOf()
    )
}