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
import com.aplications.parcial2.data.model.Category
import com.aplications.parcial2.data.model.Place
import java.util.UUID

@Composable
fun AgregarLugar(
    navController: NavController,
    tripId: String,
    lugares: MutableList<Place>
) {
    Scaffold { padding ->
        BodyAgregarLugar(
            modifier = Modifier.padding(padding),
            navController = navController,
            tripId = tripId,
            lugares = lugares
        )
    }
}

@Composable
fun BodyAgregarLugar(
    modifier: Modifier = Modifier,
    navController: NavController,
    tripId: String,
    lugares: MutableList<Place>
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var menu by remember { mutableStateOf(false) }
    var categoria by remember { mutableStateOf<Category?>(null) }

    val categorias = listOf(
        Category(1, "Historico", "Lugar historico"),
        Category(2, "Natural", "Lugar natural"),
        Category(3, "Cultural", "Lugar cultural")
    )

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
            Text("Agregar lugar", color = Color.White, fontSize = 24.sp)
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
                label = { Text("Nombre del lugar") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripcion") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = ciudad,
                onValueChange = { ciudad = it },
                label = { Text("Ciudad") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Text("Categoria")

            Box {
                OutlinedButton(
                    onClick = { menu = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(categoria?.name ?: "Seleccionar categoria")
                }

                DropdownMenu(
                    expanded = menu,
                    onDismissRequest = { menu = false }
                ) {
                    categorias.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.name) },
                            onClick = {
                                categoria = opcion
                                menu = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha tentativa") },
                placeholder = { Text("AAAA-MM-DD") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (mensaje.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                Text(mensaje, color = Color.Red)
            }
        }

        Button(
            onClick = {
                val seleccionada = categoria

                if (nombre.isBlank() || descripcion.isBlank() ||
                    ciudad.isBlank() || fecha.isBlank() ||
                    seleccionada == null
                ) {
                    mensaje = "Complete todos los campos"
                } else {
                    lugares.add(
                        Place(
                            id = UUID.randomUUID().toString(),
                            tripId = tripId,
                            name = nombre.trim(),
                            description = descripcion.trim(),
                            city = ciudad.trim(),
                            categoryId = seleccionada.id,
                            tentativeDate = fecha.trim(),
                            category = seleccionada
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
fun PreviewAgregarLugar() {
    AgregarLugar(
        navController = rememberNavController(),
        tripId = "1",
        lugares = mutableListOf()
    )
}