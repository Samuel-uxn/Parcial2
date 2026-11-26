package com.aplications.parcial2.data.model

//Categories
data class Category(
    val id: Int,
    val name: String,
    val description: String
)
//trips
data class Trip(
    val id: String,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)
data class TripDetail(
    val id: String,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val places: List<Place> = emptyList(),
    val placesCount: Int = 0
)
data class CreateTripRequest(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)
//ṕlaces
data class Place(
    val id: String,
    val tripId: String,
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int,
    val tentativeDate: String,
    val category: Category? = null
)
data class CreatePlaceRequest(
    val tripId: String,
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int,
    val tentativeDate: String
)//errios
data class ErrorBody(val message: String?)