package com.smartbank.smarthamrah.features.home

data class CustomerHomeProfile(
    val customerId: Long = 1,
    val customerName: String = "فاطمه عزیزی",
    val customerNumber: String = "6000000006",
    val accountTypeTitle: String = "حقیقی",
    val accountOpeningDate: String = "1400/09/11",
    val branchName: String = "شعبه فلسطين",
    val branchCode: String = "4245",
    val mobileNumber: String = "09124245539",
    val nationalCode: String = "0081063431",
    val callCount: Int = 0,
    val messageCount: Int = 0
)