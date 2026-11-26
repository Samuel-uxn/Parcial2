package com.aplications.parcial2.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.aplications.parcial2.presentation.viewmodel.CreateTripUiState
import com.aplications.parcial2.presentation.viewmodel.CreateTripViewModel

@Composable
fun CrearViaje(
    navController: NavController,
    viewModel: CreateTripViewModel = viewModel(
        factory = viewModelFactory { initializer { CreateTripViewModel(AppContainer.repository) } }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Cuando el viaje se guardó en el backend, volvemos a la lista
    LaunchedEffect(state.saved) { if (state.saved) navController.popBackStack() }

    Scaffold { padding ->
        BodyCrearViaje(
            modifier = Modifier.padding(padding),
            state = state,
            onNombre = viewModel::onNameChange,
            onDestino = viewModel::onDestinationChange,
            onFechaInicio = viewModel::onStartDateChange,
            onFechaFin = viewModel::onEndDateChange,
            onGuardar = viewModel::saveTrip,
            onVolver = { navController.popBackStack() }
        )
    }
}

@Composable
fun BodyCrearViaje(
    modifier: Modifier = Modifier,
    state: CreateTripUiState,
    onNombre: (String) -> Unit,
    onDestino: (String) -> Unit,
    onFechaInicio: (String) -> Unit,
    onFechaFin: (String) -> Unit,
    onGuardar: () -> Unit,
    onVolver: () -> Unit
) {
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
            Text("Crear viaje", fontSize = 24.sp, color = Color.White)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            CampoTexto(
                label = "Nombre del viaje",
                value = state.name,
                onValueChange = onNombre,
                error = state.errors["name"]
            )

            Spacer(Modifier.height(15.dp))

            CampoTexto(
                label = "Destino",
                value = state.destination,
                onValueChange = onDestino,
                error = state.errors["destination"]
            )

            Spacer(Modifier.height(15.dp))

            Row {
                CampoTexto(
                    label = "Fecha inicio",
                    value = state.startDate,
                    onValueChange = onFechaInicio,
                    modifier = Modifier.weight(1f),
                    error = state.errors["startDate"],
                    placeholder = "AAAA-MM-DD"
                )

                Spacer(Modifier.width(10.dp))

                CampoTexto(
                    label = "Fecha fin",
                    value = state.endDate,
                    onValueChange = onFechaFin,
                    modifier = Modifier.weight(1f),
                    error = state.errors["endDate"],
                    placeholder = "AAAA-MM-DD"
                )
            }

            // Error que viene del servidor o de la red
            if (state.error != null) {
                Spacer(Modifier.height(15.dp))
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

@Preview(showBackground = true)
@Composable
fun PreviewCrearViaje() {
    BodyCrearViaje(
        state = CreateTripUiState(),
        onNombre = {}, onDestino = {}, onFechaInicio = {}, onFechaFin = {},
        onGuardar = {}, onVolver = {}
    )
}