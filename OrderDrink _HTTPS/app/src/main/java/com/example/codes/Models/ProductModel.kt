package com.example.codes.Models

import java.io.Serializable

open class ProductModel : Serializable {
    var id: String? = null
    var name: String? = null
    var price = 0
    var imageUrl: String? = null
    var discount = 0
    var category: String? = null

    constructor()
    constructor(name: String?, price: Int, imageUrl: String?, discount: Int, category: String?) {
        this.name = name
        this.price = price
        this.imageUrl = imageUrl
        this.discount = discount
        this.category = category
    }

    constructor(name: String?, price: Int, imageUrl: String?, discount: Int, category: String?,id: String?) {
        this.id = id
        this.name = name
        this.price = price
        this.imageUrl = imageUrl
        this.discount = discount
        this.category = category
    }




    val finalPrice: Double
        get() = price * (1 - discount / 100.0)

    override fun toString(): String {
        return "ProductModel(name=$name, price=$price, imageUrl=$imageUrl, discount=$discount, category=$category)"
    }
}
