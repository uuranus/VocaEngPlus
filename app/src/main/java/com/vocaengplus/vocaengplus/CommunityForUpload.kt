package com.vocaengplus.vocaengplus


data class CommunityForUpload (var categoryname:String, var profile:String, var categorywriter:String,var categorywritertoken:String, var uploadDate:String, var description:String, var like:Int, var likes:HashMap<String,Boolean>?= HashMap(), var download:Int, var downloads:HashMap<String,Boolean>?= HashMap(), var words:ArrayList<Voca>){
    constructor():this("","","","","","",0,HashMap<String,Boolean>(),0,HashMap<String,Boolean>(),ArrayList<Voca>())
}