package com.vocaengplus.vocaengplus.model.data

import java.io.Serializable

data class CategoryForEdit(var categoryname:String, var categorywriter:String, var categorywritertoken:String, var downloadDate:String, var description:String, var words:HashMap<String, Voca>): Serializable {
    constructor():this("","","","","",HashMap<String, Voca>())
}
