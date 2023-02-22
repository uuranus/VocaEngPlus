package com.vocaengplus.vocaengplus.model.data

import java.io.Serializable

data class Category(var categoryname:String, var categorywriter:String, var categorywritertoken:String, var downloadDate:String, var description:String, var words:ArrayList<Voca>):Serializable {
    constructor():this("","","","","",ArrayList<Voca>())
}