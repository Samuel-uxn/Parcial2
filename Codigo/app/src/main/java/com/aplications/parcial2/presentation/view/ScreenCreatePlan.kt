package com.aplications.planes.presentation.createplan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aplications.planes.data.remote.RetrofitClient
import com.aplications.planes.data.repository.SavingsRepositoryImpl
import createplan.CreatePlanViewModel
import createplan.CreatePlanViewModelFactory

@Composable
fun CreatePlan(
    onCreated: () -> Unit = {},
    viewModel: CreatePlanViewModel = viewModel(
        factory = CreatePlanViewModelFactory(SavingsRepositoryImpl(RetrofitClient.api))
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.created) {
        if (state.created) onCreated()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear plan", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = state.motivo,
            onValueChange = viewModel::onMotivoChange,
            label = { Text("Motivo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = state.meta,
            onValueChange = viewModel::onMetaChange,
            label = { Text("Meta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = state.meses,
            onValueChange = viewModel::onMesesChange,
            label = { Text("Meses") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = state.miembros,
            onValueChange = viewModel::onMiembrosChange,
            label = { Text("Miembros (separados por coma)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(
            onClick = viewModel::crear,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.isLoading) "Creando..." else "Crear")
        }
    }
}