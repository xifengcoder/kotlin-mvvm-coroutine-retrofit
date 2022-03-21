package com.android.coroutine.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.coroutine.repository.MainRepository
import kotlinx.coroutines.*

class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {
    companion object {
        const val TAG = "MainViewModel"
    }

    val errorMessage = MutableLiveData<String>()
    val movieList = MutableLiveData<List<Movie>>()
    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun getAllMovies() {
        Log.d(TAG, "getAllMovies 111")
        job = CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
            Log.d(TAG, "getAllMovies 222")
            val response = mainRepository.getAllMovies()
            Log.d(TAG, "getAllMovies success: ${response.body()}")

            if (response.isSuccessful) {
                movieList.value = response.body()
                loading.value = false
            } else {
                onError("Error : ${response.message()} ")
            }
        }
    }

    private fun onError(message: String) {
        Log.d(TAG, "onError message: $message")
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
        job?.cancel()
    }
}