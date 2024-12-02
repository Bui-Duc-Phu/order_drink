package com.example.codes.network.dto.request

data class AddCartRequest (
    var productId: String = "",
    var userId: String="",
    var quantity: String = "",
    var sizePrice: String="",
    var size: String="",
)