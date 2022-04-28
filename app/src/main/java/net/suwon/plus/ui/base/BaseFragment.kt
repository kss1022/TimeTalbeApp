package net.suwon.plus.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : DaggerFragment(){

    abstract val viewModelFactory: ViewModelProvider.Factory
    abstract val viewModel: VM

    protected lateinit var binding: VB
    abstract fun getViewBinding() : VB

    private lateinit var fetchJob: Job




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initState()
    }


    override fun onDestroy() {
        if(fetchJob.isActive){
            fetchJob.cancel()
        }

        super.onDestroy()
    }

    open fun initState() {
        arguments?.let {
            viewModel.storeState(it)
        }

        initViews()
        fetchJob = viewModel.fetchData()
        observeData()
    }

    open fun initViews() = Unit

    abstract fun observeData()
}