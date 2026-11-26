package com.aplications.parcial2.domain.repository

import com.aplications.parcial2.data.model.Category
import com.aplications.parcial2.data.model.CreatePlaceRequest
import com.aplications.parcial2.data.model.CreateTripRequest
import com.aplications.parcial2.data.model.Place
import com.aplications.parcial2.data.model.Trip
import com.aplications.parcial2.data.model.TripDetail

interface TripRepository {
    suspend fun getTrips(): Result<List<Trip>>
    suspend fun getTripDetail(id: String): Result<TripDetail>
    suspend fun saveTrip(request: CreateTripRequest): Result<Trip>
}

interface PlaceRepository {
    suspend fun getPlacesByTrip(tripId: String): Result<List<Place>>
    suspend fun savePlace(request: CreatePlaceRequest): Result<Place>
}

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
}