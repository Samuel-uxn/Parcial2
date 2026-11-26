package com.aplications.planes.data.remote

import com.aplications.parcial2.data.model.Category
import com.aplications.parcial2.data.model.CreatePlaceRequest
import com.aplications.parcial2.data.model.CreateTripRequest
import com.aplications.parcial2.data.model.Place
import com.aplications.parcial2.data.model.Trip
import com.aplications.parcial2.data.model.TripDetail
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService{
    @GET("trips")
    suspend fun getTrips(): List<Trip>

    @POST("trips")
    suspend fun createTrip(@Body body: CreateTripRequest): Trip

    @GET("trips/{id}")
    suspend fun getTripDetail(@Path("id") id: String): TripDetail

    @GET("places")
    suspend fun getPlaces(@Query("tripId") tripId: String? = null): List<Place>

    @POST("places")
    suspend fun createPlace(@Body body: CreatePlaceRequest): Place

    @GET("categories")
    suspend fun getCategories(): List<Category>
}