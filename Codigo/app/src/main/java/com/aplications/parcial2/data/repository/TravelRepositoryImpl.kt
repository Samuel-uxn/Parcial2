package com.aplications.parcial2.data.repository


import com.aplications.parcial2.data.model.Category
import com.aplications.parcial2.data.model.CreatePlaceRequest
import com.aplications.parcial2.data.model.CreateTripRequest
import com.aplications.parcial2.data.model.ErrorBody
import com.aplications.parcial2.data.model.Place
import com.aplications.parcial2.data.model.Trip
import com.aplications.parcial2.data.model.TripDetail
import com.aplications.parcial2.domain.repository.CategoryRepository
import com.aplications.parcial2.domain.repository.PlaceRepository
import com.aplications.parcial2.domain.repository.TripRepository
import com.aplications.planes.data.remote.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class TravelRepositoryImpl(
    private val api: ApiService
) : TripRepository, PlaceRepository, CategoryRepository {

    override suspend fun getTrips(): Result<List<Trip>> =
        safeCall { api.getTrips() }

    override suspend fun getTripDetail(id: String): Result<TripDetail> =
        safeCall { api.getTripDetail(id) }

    override suspend fun saveTrip(request: CreateTripRequest): Result<Trip> =
        safeCall { api.createTrip(request) }

    override suspend fun getPlacesByTrip(tripId: String): Result<List<Place>> =
        safeCall { api.getPlaces(tripId) }

    override suspend fun savePlace(request: CreatePlaceRequest): Result<Place> =
        safeCall { api.createPlace(request) }

    override suspend fun getCategories(): Result<List<Category>> =
        safeCall { api.getCategories() }

    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: HttpException) {
            val msg = runCatching {
                Gson().fromJson(e.response()?.errorBody()?.string(), ErrorBody::class.java)?.message
            }.getOrNull()
            Result.failure(Exception(msg ?: "Error del servidor (${e.code()})"))
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor. "))
        } catch (e: Exception) {
            Result.failure(e)
        }
}
