package com.vocaengplus.vocaengplus.model.data.new

import android.net.Uri

data class UserAuth(
    val uid: String,
    val emailId: String = "",
    val name: String,
    val photoUrl: Uri?
)
