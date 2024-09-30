package com.example.codes.Administrator.model

data class ItemBill(
    val date: String,
    val name: String,
    val price: Double,
    val orderID: String, // Add this
    val uID: String // Add this
)