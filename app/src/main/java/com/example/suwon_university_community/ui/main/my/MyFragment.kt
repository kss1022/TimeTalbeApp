package com.example.suwon_university_community.ui.main.my

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.databinding.FragmentMyBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: MyViewModel by viewModels<MyViewModel> { viewModelFactory }


    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun initViews() {
        super.initViews()
    }

    override fun observeData() {
    }

    companion object{
        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"
    }
}