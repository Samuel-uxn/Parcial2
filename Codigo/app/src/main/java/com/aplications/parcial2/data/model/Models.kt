package com.aplications.planes.data.model

import com.google.gson.annotations.SerializedName
data class Plan(
    @SerializedName("_id") val id:String,
    val name:String,
    val motive: String?,
    val targetAmount: Double,
    val months: Int,
)
data class CreatePlanRequest(
    val name:String,
    val motive:String,
    val targetAmount: Double,
    val months: Int
)

data class Member(
    @SerializedName("_id") val id:String,
    val name: String,
    val planId:String,
    val contributionPerMonth:Double?,
)
data class CreateMemberRequest(
    val name: String,
    val planId:String,
    val contributionPerMonth:Double,
)

data class CreatePayRequest(
    val planId:String,
    val memberId: String,
    val amount: Double
)
data class Payments(
    @SerializedName("_id") val id:String,
    val planId:String,
    val memberId: String,
    val amount: Double,
    val date: String?
)