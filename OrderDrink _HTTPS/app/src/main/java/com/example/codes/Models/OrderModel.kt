package com.example.codes.Models

class OrderModel {
    var orderId: String? = null
    var totalPrice: String? = null
    var dateTime: String? = null
    var orderDetails: ArrayList<CartModel>? = null

    // Constructors
    constructor()
    constructor(
        orderId: String?,
        totalPrice: String?,
        dateTime: String?,
        orderDetails: ArrayList<CartModel>?
    ) {
        this.orderId = orderId
        this.totalPrice = totalPrice
        this.dateTime = dateTime
        this.orderDetails = orderDetails
    }
}
