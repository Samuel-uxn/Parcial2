package com.aplications.planes.data.remote

import com.aplications.planes.data.model.CreateMemberRequest
import com.aplications.planes.data.model.CreatePayRequest
import com.aplications.planes.data.model.CreatePlanRequest
import com.aplications.planes.data.model.Member
import com.aplications.planes.data.model.Payments
import com.aplications.planes.data.model.Plan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    //miembros
    @GET("members/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): List<Member>
    @POST("members")
    suspend fun createMember(@Body body: CreateMemberRequest): Member
    @GET("members")
    suspend fun getMember(): List<Member>

    //Planes
    @GET("plans/{id}")
    suspend fun getPlanById(@Path("id") id: String): Plan

    @GET("plans")
    suspend fun getPlan(): List<Plan>

    @POST("plans")
    suspend fun createPlan(@Body body: CreatePlanRequest): Plan

    //Pagos
    @GET("payments/plan/{planId}")
    suspend fun getPaymentsByPlan(@Path("planId")planId: String): Response<List<Payments>>

    @POST("payments")
    suspend fun createPayment(@Body body: CreatePayRequest): Payments

}