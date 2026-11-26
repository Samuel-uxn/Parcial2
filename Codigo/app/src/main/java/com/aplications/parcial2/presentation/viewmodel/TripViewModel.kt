package com.aplications.parcial2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplications.parcial2.data.model.Trip
import com.aplications.parcial2.domain.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TripListUiState(
    val isLoading: Boolean = false,
    val trips: List<Trip> = emptyList(),
    val error: String? = null
)

/** ViewModel de la lista de viajes. */
class TripViewModel(private val repository: TripRepository) : ViewModel() {

    private val _state = MutableStateFlow(TripListUiState())
    val state: StateFlow<TripListUiState> = _state.asStateFlow()

    fun loadTrips() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.getTrips()
                .onSuccess { trips -> _state.update { it.copy(isLoading = false, trips = trips) } }
                .onFailure { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}