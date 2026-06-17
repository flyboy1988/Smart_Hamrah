package com.smartbank.smarthamrah.features.customers.domain.model

data class BankerCustomer(
    val customerId: Long,
    val fullName: String,
    val customerNumber: String,
    val accountTypeTitle: String,
    val accountOpeningDate: String,
    val branchName: String,
    val branchCode: String,
    val mobileNumber: String,
    val nationalCode: String,
    val callCount: Int,
    val messageCount: Int
)