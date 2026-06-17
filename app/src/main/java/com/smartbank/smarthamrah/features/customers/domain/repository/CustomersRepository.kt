package com.smartbank.smarthamrah.features.customers.domain.repository

import com.smartbank.smarthamrah.features.customers.data.dto.BankerCustomersRequestDto
import com.smartbank.smarthamrah.features.customers.domain.model.BankerCustomer
import com.smartbank.smarthamrah.features.customers.domain.model.BankerCustomersRequest

interface CustomersRepository {
    suspend fun getBankerCustomers(request: BankerCustomersRequestDto?): List<BankerCustomer>
}
