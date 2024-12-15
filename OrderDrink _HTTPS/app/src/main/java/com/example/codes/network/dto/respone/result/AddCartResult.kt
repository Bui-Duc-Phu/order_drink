package com.example.codes.network.dto.respone.result

data class AddCartResult (
    val productId: String,
    val userId: String,
    val quantity: String,
    val sizePrice: String,
    val size: String,
)