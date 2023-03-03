package com.vocaengplus.vocaengplus.network

interface NetworkCallback {
    fun <T> onDataLoaded(data:T)
    fun <T> onDataFailed(exception:T)
}