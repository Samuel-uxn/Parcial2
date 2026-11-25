package com.aplications.planes.data.model

import com.google.gson.annotations.SerializedName
//biajes
data class Trips(
    @SerializedName("id") val id: String,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,

)
data class CreateTripsRequest(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)
//places
data class Places(
    @SerializedName("id") val id: String
)
data class CreatePlacesRequest(
   val tripId: String,
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int,
    val tentativeDate: String
)
//categorias
data class Categories(
    @SerializedName("id") val id: String,
    val name: String,
    val description: String
)