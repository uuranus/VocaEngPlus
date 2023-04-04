package com.vocaengplus.vocaengplus.network

interface  NetworkCallback<T> {
    fun onDataLoaded(data:T)
    fun onDataFailed(exception:T)
}