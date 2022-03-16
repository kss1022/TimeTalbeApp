package com.example.suwon_university_community.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Job

abstract class BaseActivity <VM : BaseViewModel, VB : ViewBinding> : DaggerAppCompatActivity(){

    abstract var viewModelFactory: ViewModelProvider.Factory
    abstract val viewModel : VM

    protected lateinit var binding: VB
    abstract fun getViewBinding() : VB


    private lateinit var fetchJob : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        initState()
    }

    override fun onDestroy() {
        if(fetchJob.isActive) {
            fetchJob.cancel()
        }

        super.onDestroy()
    }


    abstract fun observeData()



    open fun initState(){
        initView()
        fetchJob = viewModel.fetchData()
        observeData()
    }

    open fun initView() = Unit
}