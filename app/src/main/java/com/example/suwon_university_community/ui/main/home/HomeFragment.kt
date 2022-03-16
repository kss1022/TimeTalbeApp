package com.example.suwon_university_community.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.databinding.FragmentHomeBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: HomeViewModel by viewModels<HomeViewModel> { viewModelFactory }


    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun initViews() {
        super.initViews()
    }

    override fun observeData() {
    }

    companion object{
        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }
}