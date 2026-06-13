package com.akeyma.ewallet.model

data class PaymentMethod(
    val name: String,
    val detail: String,
    val emoji: String,
    val isActive: Boolean
)