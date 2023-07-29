package com.vocaengplus.vocaengplus.model.data.dataSource

import com.vocaengplus.vocaengplus.network.SearchAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchDataSource @Inject constructor(
    private val service: SearchAPIService,
) {

    suspend fun translate(source: String, target: String, text: String): Result<String> {
        return withContext(Dispatchers.IO) {
            val networkResponse = service.translate(source, target, text)
            println("Text $text")
            println("network ${networkResponse}")
            if (networkResponse.isSuccess) {
                networkResponse.getOrNull()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception())
            } else {
                Result.failure(Exception())
            }
        }
    }
}