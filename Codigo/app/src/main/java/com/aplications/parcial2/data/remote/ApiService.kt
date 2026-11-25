package com.aplications.planes.data.remote

import com.aplications.planes.data.model.Categories
import com.aplications.planes.data.model.CreatePlacesRequest
import com.aplications.planes.data.model.CreateTripsRequest
import com.aplications.planes.data.model.Places
import com.aplications.planes.data.model.Trips
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService{
    @GET("trips")
    suspend fun getTrips(): List<Trips>
    @POST("trips")
    suspend fun CrateTrips(@Body body: CreateTripsRequest): Trips
    @GET("trips/{id}")
    suspend fun getTripsById(@Path("id")id: String): List<Trips>

    @POST("places")
    suspend fun CreatePlaces(@Body body: CreatePlacesRequest): Places
    @GET("planes")
    suspend fun getPlaces(): List<Places>

    @GET("categories")
    suspend fun getCategories(): List<Categories>
}