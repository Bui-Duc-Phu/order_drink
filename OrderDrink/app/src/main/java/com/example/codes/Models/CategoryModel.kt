package com.example.codes.Models

class CategoryModel {
    var name: String? = null
        private set

    constructor()
    constructor(name: String?) {
        this.name = name
    }
}
