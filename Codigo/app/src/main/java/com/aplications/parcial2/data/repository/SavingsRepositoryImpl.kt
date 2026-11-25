package com.aplications.planes.data.repository

import com.aplications.planes.data.model.CreateMemberRequest
import com.aplications.planes.data.model.CreatePayRequest
import com.aplications.planes.data.model.CreatePlanRequest
import com.aplications.planes.data.model.Member
import com.aplications.planes.data.model.Payments
import com.aplications.planes.data.model.Plan
import com.aplications.planes.data.remote.ApiService
import retrofit2.HttpException

class SavingsRepositoryImpl(
    private val api: ApiService
) : SavingsRepository {

    override suspend fun getPlans(): Result<List<Plan>> {
        return runCatching { api.getPlan() }
    }

    override suspend fun getPlanById(id: String): Result<Plan> {
        return runCatching { api.getPlanById(id) }
    }

    override suspend fun getMembers(planId: String): Result<List<Member>> {
        return runCatching { api.getMembersByPlan(planId) }
    }

    override suspend fun registerPayment(
        planId: String,
        memberId: String,
        amount: Double
    ): Result<Payments> {
        return runCatching { api.createPayment(CreatePayRequest(
            planId = planId,
            memberId = memberId,
            amount = amount
        )) }
    }

    override suspend fun getPayments(planId: String): Result<List<Payments>> {
        return runCatching {
            val response = api.getPaymentsByPlan(planId)
            when {
                response.isSuccessful -> response.body().orEmpty()
                response.code() == 404 -> emptyList()
                else -> throw HttpException(response)
            }
        }
    }

    override suspend fun createPlan(
        name: String,
        motive: String,
        targetAmount: Double,
        months: Int,
        memberNames: List<String>
    ): Result<Plan> {
        return runCatching {
            val plan = api.createPlan(
                CreatePlanRequest(
                    name = name,
                    motive = motive,
                    targetAmount = targetAmount,
                    months = months
                )
            )

            if (memberNames.isNotEmpty()) {
                val cuota = targetAmount / months / memberNames.size
                memberNames.forEach { nombre ->
                    api.createMember(
                        CreateMemberRequest(
                            name = nombre,
                            planId = plan.id,
                            contributionPerMonth = cuota
                        )
                    )
                }
            }

            plan
        }
    }

}