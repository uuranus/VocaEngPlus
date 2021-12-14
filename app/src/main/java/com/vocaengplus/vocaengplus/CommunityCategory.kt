package com.vocaengplus.vocaengplus


data class CommunityCategory(var categoryname:String, var profile:String, var categorywriter:String, var categorywritertoken:String, var uploadDate:String, var description:String, var like:Int, var isLiked:Boolean, var download:Int, var isDownloaded:Boolean, var words:ArrayList<Voca>){
    constructor():this("","","","", "","",0,false,0,false,ArrayList<Voca>())
}
