package com.example.sellingapp.model

class UserData {
    var userImage: String?=null
    var userName: String?=null

    constructor(userImage: String?, userName: String?) {
        this.userImage=userImage
        this.userName=userName
    }
    constructor()
}
