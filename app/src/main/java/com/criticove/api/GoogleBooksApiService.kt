package com.criticove.api

import retrofit2.http.GET
import retrofit2.http.Query


interface GoogleBooksApiService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String, @Query("key") apiKey: String): BookSearchResponse
}

data class BookSearchResponse(val items: List<BookItem>)

data class BookItem(val id: String, val volumeInfo: VolumeInfo)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val publishedDate: String,
    val categories: List<String>?
)
