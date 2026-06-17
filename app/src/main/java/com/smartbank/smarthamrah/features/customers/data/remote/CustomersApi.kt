package com.smartbank.smarthamrah.features.customers.data.remote

import com.smartbank.smarthamrah.features.customers.data.dto.BankerCustomerDto
import com.smartbank.smarthamrah.features.customers.data.dto.BankerCustomersRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomersApi {
    @POST("api/v1/Users/banker-customers")
    suspend fun getBankerCustomers(
        @Body request: BankerCustomersRequestDto?
    ): List<BankerCustomerDto>
}