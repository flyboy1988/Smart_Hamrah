package com.smartbank.smarthamrah.features.customers.data.dto

import com.google.gson.annotations.SerializedName

data class BankerCustomersRequestDto(
    @SerializedName("customerName")
    val customerName: String? = null
)