package com.smartbank.smarthamrah.features.customers.data.repository

import com.smartbank.smarthamrah.features.customers.data.dto.BankerCustomerDto
import com.smartbank.smarthamrah.features.customers.data.dto.BankerCustomersRequestDto
import com.smartbank.smarthamrah.features.customers.data.mapper.toDomain
import com.smartbank.smarthamrah.features.customers.data.remote.CustomersApi
import com.smartbank.smarthamrah.features.customers.domain.model.BankerCustomer
import com.smartbank.smarthamrah.features.customers.domain.model.BankerCustomersRequest
import com.smartbank.smarthamrah.features.customers.domain.repository.CustomersRepository
import javax.inject.Inject
import kotlin.collections.map

class CustomersRepositoryImpl @Inject constructor(
    private val api: CustomersApi
) : CustomersRepository {


    override suspend fun getBankerCustomers(
        request: BankerCustomersRequestDto?
    ): List<BankerCustomer> {
        return api.getBankerCustomers(request).map(BankerCustomerDto::toDomain)
    }
}
