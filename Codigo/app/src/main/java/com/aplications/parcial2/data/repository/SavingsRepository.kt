package com.aplications.planes.data.repository

import com.aplications.planes.data.model.Trips

interface SavingsRepository {
    suspend fun getTrips(): Result<List<Trips>>

    
    /*
    suspend fun getPlanById(id: String): Result<Plan>

    suspend fun getMembers(planId: String): Result<List<Member>>

    suspend fun registerPayment(planId: String, memberId: String, amount: Double): Result<Payments>

    suspend fun getPayments(planId: String): Result<List<Payments>>

    suspend fun createPlan(
        name: String,
        motive: String,
        targetAmount: Double,
        months: Int,
        memberNames: List<String>
    ): Result<Plan>



     */

}