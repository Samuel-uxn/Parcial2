package com.aplications.parcial2.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.aplications.parcial2.data.model.TripDetail
import com.aplications.parcial2.presentation.viewmodel.AddPlaceUiState
import com.aplications.parcial2.presentation.viewmodel.AddPlaceViewModel

@Composable
fun AgregarLugar(
    navController: NavController,
    tripId: String,
    viewModel: AddPlaceViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                AddPlaceViewModel(
                    AppContainer.repository, AppContainer.repository, AppContainer.repository, tripId
                )
            }
        }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Cuando el lugar se guardó en el backend, volvemos al detalle
    LaunchedEffect(state.saved) { if (state.saved) navController.popBackStack() }

    Scaffold { padding ->
        BodyAgregarLugar(
            modifier = Modifier.padding(padding),
            state = state,
            onNombre = viewModel::onNameChange,
            onDescripcion = viewModel::onDescriptionChange,
            onCiudad = viewModel::onCityChange,
            onCategoria = viewModel::onCategorySelected,
            onFecha = viewModel::onDateChange,
            onGuardar = viewModel::savePlace,
            onReintentar = viewModel::load,
            onVolver = { navController.popBackStack() }
        )
    }
}

@Composable
fun BodyAgregarLugar(
    modifier: Modifier = Modifier,
    state: AddPlaceUiState,
    onNombre: (String) -> Unit,
    onDescripcion: (String) -> Unit,
    onCiudad: (String) -> Unit,
    onCategoria: (Int) -> Unit,
    onFecha: (String) -> Unit,
    onGuardar: () -> Unit,
    onReintentar: () -> Unit,
    onVolver: () -> Unit
) {
    var menu by remember { mutableStateOf(false) }
    val categoria = state.categories.firstOrNull { it.id == state.categoryId }
    val viaje = state.trip

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
            TextButton(onClick = onVolver) {
                Text("←", color = Color.White, fontSize = 24.sp)
            }
            Text("Agregar lugar", color = Color.White, fontSize = 24.sp)
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (viaje == null) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(state.error ?: "No se pudo cargar el viaje", color = Color.Red)
                Spacer(Modifier.height(10.dp))
                Button(onClick = onReintentar) { Text("Reintentar") }
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    "Fechas del viaje: ${viaje.startDate} a ${viaje.endDate}",
                    color = Color.Gray
                )

                Spacer(Modifier.height(10.dp))

                CampoTexto(
                    label = "Nombre del lugar",
                    value = state.name,
                    onValueChange = onNombre,
                    error = state.errors["name"]
                )

                Spacer(Modifier.height(10.dp))

                CampoTexto(
                    label = "Descripcion",
                    value = state.description,
                    onValueChange = onDescripcion,
                    error = state.errors["description"],
                    singleLine = false
                )

                Spacer(Modifier.height(10.dp))

                CampoTexto(
                    label = "Ciudad",
                    value = state.city,
                    onValueChange = onCiudad,
                    error = state.errors["city"]
                )

                Spacer(Modifier.height(10.dp))

                Text("Categoria")

                // Las categorías vienen del backend (GET /categories)
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
                        state.categories.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.name) },
                                onClick = {
                                    onCategoria(opcion.id)
                                    menu = false
                                }
                            )
                        }
                    }
                }
                val errorCategoria = state.errors["categoryId"]
                if (errorCategoria != null) {
                    Text(errorCategoria, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(Modifier.height(10.dp))

                CampoTexto(
                    label = "Fecha tentativa",
                    value = state.tentativeDate,
                    onValueChange = onFecha,
                    error = state.errors["tentativeDate"],
                    placeholder = "AAAA-MM-DD"
                )

                if (state.error != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(state.error, color = Color.Red)
                }
            }

            Button(
                onClick = onGuardar,
                enabled = !state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(if (state.isSaving) "Guardando..." else "Guardar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAgregarLugar() {
    BodyAgregarLugar(
        state = AddPlaceUiState(
            isLoading = false,
            trip = TripDetail("1", "Playa", "Cartagena", "2026-12-01", "2026-12-10"),
            categories = listOf(Category(1, "Histórico", ""), Category(2, "Natural", ""))
        ),
        onNombre = {}, onDescripcion = {}, onCiudad = {}, onCategoria = {},
        onFecha = {}, onGuardar = {}, onReintentar = {}, onVolver = {}
    )
}