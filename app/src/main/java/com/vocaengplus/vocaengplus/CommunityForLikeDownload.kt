package com.vocaengplus.vocaengplus


data class CommunityForLikeDownload (var categoryname:String, var profile:String, var categorywriter:String,var categorywritertoken:String, var uploadDate:String, var description:String, var like:Int, var likes:HashMap<String,Boolean>?= HashMap(), var download:Int, var downloads:HashMap<String,Boolean>?= HashMap(), var words:HashMap<String, Voca>){
    constructor():this("","","","","","",0,HashMap<String,Boolean>(),0,HashMap<String,Boolean>(),HashMap<String,Voca>())
}