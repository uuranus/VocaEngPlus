package com.vocaengplus.vocaengplus.network

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vocaengplus.vocaengplus.ui.util.DB_ROOT_KEY

class DatabaseService {

    private val database = Firebase.database.reference.child(DB_ROOT_KEY)

    fun getUserData(userId:String){

    }

}