package com.smartbank.smarthamrah.features.customers.data.dto

import com.google.gson.annotations.SerializedName

data class BankerCustomerDto(
    @SerializedName("customerId")
    val customerId: Long?,

    @SerializedName("customerName")
    val customerName: String?,

    @SerializedName("customerNumber")
    val customerNumber: String?,

    @SerializedName("accountTypeTitle")
    val accountTypeTitle: String?,

    @SerializedName("accountOpeningDate")
    val accountOpeningDate: String?,

    @SerializedName("branchName")
    val branchName: String?,

    @SerializedName("branchCode")
    val branchCode: String?,

    @SerializedName("mobileNumber")
    val mobileNumber: String?,

    @SerializedName("nationalCode")
    val nationalCode: String?,

    @SerializedName("callCount")
    val callCount: Int?,

    @SerializedName("messageCount")
    val messageCount: Int?
)

