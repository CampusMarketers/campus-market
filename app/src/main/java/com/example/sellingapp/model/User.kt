package com.example.sellingapp.model

class User {
    var email=""
    var password=""
    var name=""
    var imageUrl=""


    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }
}
