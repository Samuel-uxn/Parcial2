package com.aplications.parcial2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplications.parcial2.data.model.CreateTripRequest
import com.aplications.parcial2.domain.repository.TripRepository
import com.aplications.parcial2.domain.validation.TripInput
import com.aplications.parcial2.domain.validation.validateTrip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateTripUiState(
    val name: String = "",
    val destination: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

class CreateTripViewModel(private val repository: TripRepository) : ViewModel() {

    private val _state = MutableStateFlow(CreateTripUiState())
    val state: StateFlow<CreateTripUiState> = _state.asStateFlow()

    fun onNameChange(v: String) = _state.update { it.copy(name = v, errors = it.errors - "name") }
    fun onDestinationChange(v: String) = _state.update { it.copy(destination = v, errors = it.errors - "destination") }
    fun onStartDateChange(v: String) = _state.update { it.copy(startDate = v, errors = it.errors - "startDate") }
    fun onEndDateChange(v: String) = _state.update { it.copy(endDate = v, errors = it.errors - "endDate") }

    fun saveTrip() {
        val s = _state.value
        val result = validateTrip(TripInput(s.name, s.destination, s.startDate, s.endDate))
        if (!result.isValid) {
            _state.update { it.copy(errors = result.errors, error = null) }
            return   // no se persiste un registro inválido
        }
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            repository.saveTrip(
                CreateTripRequest(s.name.trim(), s.destination.trim(), s.startDate, s.endDate)
            )
                .onSuccess { _state.update { it.copy(isSaving = false, saved = true) } }
                .onFailure { e -> _state.update { it.copy(isSaving = false, error = e.message) } }
        }
    }
}