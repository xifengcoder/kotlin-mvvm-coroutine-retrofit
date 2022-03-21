package com.android.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.coroutine.api.ApiService
import com.android.coroutine.databinding.ActivityMainBinding
import com.android.coroutine.viewmodel.MainViewModel
import com.android.coroutine.viewmodel.MyViewModelFactory
import com.android.coroutine.repository.MainRepository

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private val adapter = MovieAdapter()
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(MainRepository(ApiService.getInstance()))
        )
            .get(MainViewModel::class.java)

        initObserver()
    }

    private fun initObserver() {
        viewModel.movieList.observe(this, {
            adapter.setMovies(it)
        })

        viewModel.errorMessage.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })

        viewModel.getAllMovies()
    }
}