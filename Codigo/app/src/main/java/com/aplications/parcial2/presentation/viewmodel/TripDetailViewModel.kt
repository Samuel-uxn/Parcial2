package com.aplications.parcial2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplications.parcial2.data.model.TripDetail
import com.aplications.parcial2.domain.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TripDetailUiState(
    val isLoading: Boolean = false,
    val trip: TripDetail? = null,
    val error: String? = null
)

class TripDetailViewModel(
    private val repository: TripRepository,
    private val tripId: String
) : ViewModel() {

    private val _state = MutableStateFlow(TripDetailUiState())
    val state: StateFlow<TripDetailUiState> = _state.asStateFlow()

    fun loadTrip() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.getTripDetail(tripId)
                .onSuccess { t -> _state.update { it.copy(isLoading = false, trip = t) } }
                .onFailure { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}