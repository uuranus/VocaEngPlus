package com.vocaengplus.vocaengplus.model.data.new

data class UserData(
    val nickname:String,
    val uid:String,
    val idToken:String,
    val lastLogin:Long,
    val isGuest:Boolean
)
