package com.vocaengplus.vocaengplus.model.repository

import com.vocaengplus.vocaengplus.model.dataSource.SearchDataSource
import javax.inject.Inject

class SearchRepository @Inject constructor(private val dataSource: SearchDataSource) {

    suspend fun translate(source: String, target: String, text: String): Result<String> {
        val networkResponse = dataSource.translate(source, target, text)
        return if (networkResponse.isSuccess) {
            networkResponse.getOrNull()?.let {
                Result.success(it)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }
}