package com.vocaengplus.vocaengplus

data class CommunityWriter(var totalLike:Int,var totalLiked:Int, var totalUpload:Int, var totalDownloaded:Int,var uploads:HashMap<String,Boolean>) {
    constructor():this(0,0,0,0,HashMap<String,Boolean>())
}