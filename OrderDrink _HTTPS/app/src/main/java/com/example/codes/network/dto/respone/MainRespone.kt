package com.example.codes.network.dto.respone

data class MainRespone <T>(
    val message: String,
    val result: T?
)