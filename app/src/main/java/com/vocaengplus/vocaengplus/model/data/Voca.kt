package com.vocaengplus.vocaengplus.model.data

import java.io.Serializable

data class Voca(var category:String, var word:String, var meaning:String, var checked:Int):Serializable{
    constructor():this("","hello", "안녕",0)
}