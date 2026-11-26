package com.aplications.parcial2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplications.parcial2.data.model.Category
import com.aplications.parcial2.data.model.CreatePlaceRequest
import com.aplications.parcial2.data.model.TripDetail
import com.aplications.parcial2.domain.repository.CategoryRepository
import com.aplications.parcial2.domain.repository.PlaceRepository
import com.aplications.parcial2.domain.repository.TripRepository
import com.aplications.parcial2.domain.validation.PlaceInput
import com.aplications.parcial2.domain.validation.validatePlace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddPlaceUiState(
    val isLoading: Boolean = true,
    val trip: TripDetail? = null,
    val categories: List<Category> = emptyList(),
    val name: String = "",
    val description: String = "",
    val city: String = "",
    val categoryId: Int? = null,
    val tentativeDate: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

class AddPlaceViewModel(
    private val tripRepository: TripRepository,
    private val categoryRepository: CategoryRepository,
    private val placeRepository: PlaceRepository,
    private val tripId: String
) : ViewModel() {

    private val _state = MutableStateFlow(AddPlaceUiState())
    val state: StateFlow<AddPlaceUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val tripResult = tripRepository.getTripDetail(tripId)
            val categoriesResult = categoryRepository.getCategories()
            val failure = tripResult.exceptionOrNull() ?: categoriesResult.exceptionOrNull()
            _state.update {
                it.copy(
                    isLoading = false,
                    trip = tripResult.getOrNull(),
                    categories = categoriesResult.getOrDefault(emptyList()),
                    error = failure?.message
                )
            }
        }
    }

    fun onNameChange(v: String) = _state.update { it.copy(name = v, errors = it.errors - "name") }
    fun onDescriptionChange(v: String) = _state.update { it.copy(description = v, errors = it.errors - "description") }
    fun onCityChange(v: String) = _state.update { it.copy(city = v, errors = it.errors - "city") }
    fun onCategorySelected(id: Int) = _state.update { it.copy(categoryId = id, errors = it.errors - "categoryId") }
    fun onDateChange(v: String) = _state.update { it.copy(tentativeDate = v, errors = it.errors - "tentativeDate") }

    fun savePlace() {
        val s = _state.value
        val trip = s.trip
        if (trip == null) {
            _state.update { it.copy(error = "No se pudo cargar el viaje. Intenta de nuevo.") }
            return
        }
        val result = validatePlace(
            PlaceInput(s.name, s.description, s.city, s.categoryId, s.tentativeDate),
            trip.startDate, trip.endDate
        )
        val categoryId = s.categoryId
        if (!result.isValid || categoryId == null) {
            _state.update { it.copy(errors = result.errors, error = null) }
            return   // no se persiste un lugar inválido
        }
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            placeRepository.savePlace(
                CreatePlaceRequest(
                    tripId = tripId,
                    name = s.name.trim(),
                    description = s.description.trim(),
                    city = s.city.trim(),
                    categoryId = categoryId,
                    tentativeDate = s.tentativeDate
                )
            )
                .onSuccess { _state.update { it.copy(isSaving = false, saved = true) } }
                .onFailure { e -> _state.update { it.copy(isSaving = false, error = e.message) } }
        }
    }
}