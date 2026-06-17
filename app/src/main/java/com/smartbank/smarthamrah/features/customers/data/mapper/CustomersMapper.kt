package com.smartbank.smarthamrah.features.customers.data.mapper

import com.smartbank.smarthamrah.features.customers.data.dto.BankerCustomerDto
import com.smartbank.smarthamrah.features.customers.domain.model.BankerCustomer

fun BankerCustomerDto.toDomain(): BankerCustomer {
    return BankerCustomer(
        customerId = customerId ?: 0L,
        fullName = customerName.orEmpty(),
        customerNumber = customerNumber.orEmpty(),
        accountTypeTitle = accountTypeTitle.orEmpty(),
        accountOpeningDate = accountOpeningDate.orEmpty(),
        branchName = branchName.orEmpty(),
        branchCode = branchCode.orEmpty(),
        mobileNumber = mobileNumber.orEmpty(),
        nationalCode = nationalCode.orEmpty(),
        callCount = callCount ?: 0,
        messageCount = messageCount ?: 0
    )
}