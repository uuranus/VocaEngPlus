package com.vocaengplus.vocaengplus.model.data

import com.vocaengplus.vocaengplus.model.data.Category

data class UserAccount (var emailId : String, var nickname : String, var token : String , var downloadNames : ArrayList<String>, var downloadData:ArrayList<Category>){
    //token : Firebase 고유 토큰 (식별자)
}