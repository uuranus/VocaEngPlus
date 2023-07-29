package com.vocaengplus.vocaengplus.model.data

sealed interface NetworkState<T>

data class Success<T>(val result: T) : NetworkState<T>
data class Fail<T>(val message: String) : NetworkState<T>
object Loading: NetworkState<Nothing>
