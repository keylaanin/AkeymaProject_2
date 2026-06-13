package com.akeyma.ewallet.model

data class Transaction(
    val id: String,
    val title: String,
    val date: String,
    val amount: Long,
    val type: String,   // "credit" or "debit"
    val emoji: String
)