package com.smartbank.smarthamrah.features.customers.domain.usecase

import com.smartbank.smarthamrah.features.customers.data.dto.BankerCustomersRequestDto
import com.smartbank.smarthamrah.features.customers.domain.model.BankerCustomer
import com.smartbank.smarthamrah.features.customers.domain.model.BankerCustomersRequest
import com.smartbank.smarthamrah.features.customers.domain.repository.CustomersRepository
import javax.inject.Inject

class GetBankerCustomersUseCase @Inject constructor(
    private val repository: CustomersRepository
) {
    suspend operator fun invoke(customersRequestDto: BankerCustomersRequestDto? ): List<BankerCustomer> {
        return repository.getBankerCustomers(customersRequestDto)
    }
}