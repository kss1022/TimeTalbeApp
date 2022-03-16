package com.example.suwon_university_community.ui.main.time_table

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.databinding.FragmentTimeTableBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

class TimeTableFragment : BaseFragment<TimeTableViewModel, FragmentTimeTableBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: TimeTableViewModel by viewModels<TimeTableViewModel> { viewModelFactory }


    override fun getViewBinding(): FragmentTimeTableBinding = FragmentTimeTableBinding.inflate(layoutInflater)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun initViews() {
        super.initViews()
    }

    override fun observeData() {
    }

    companion object{
        fun newInstance() = TimeTableFragment()

        const val TAG = "TimeTableFragment"
    }
}