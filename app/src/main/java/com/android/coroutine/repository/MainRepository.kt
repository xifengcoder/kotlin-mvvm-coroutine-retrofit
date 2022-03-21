package com.android.coroutine.repository

import com.android.coroutine.api.ApiService

class MainRepository constructor(private val apiService: ApiService) {
    suspend fun getAllMovies() = apiService.getAllMovies()
}