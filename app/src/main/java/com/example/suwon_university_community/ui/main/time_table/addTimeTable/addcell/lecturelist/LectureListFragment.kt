package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.lecturelist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.data.entity.lecture.CollegeCategory
import com.example.suwon_university_community.databinding.FragmentLectureListBinding
import com.example.suwon_university_community.model.LectureModel
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.util.provider.DefaultResourceProvider
import com.example.suwon_university_community.widget.adapter.ModelRecyclerViewAdapter
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener
import javax.inject.Inject

class LectureListFragment : BaseFragment<LectureListViewModel, FragmentLectureListBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: LectureListViewModel by viewModels<LectureListViewModel> { viewModelFactory }


    override fun getViewBinding(): FragmentLectureListBinding =
        FragmentLectureListBinding.inflate(layoutInflater)

    @Inject
    lateinit var resourceProvider: DefaultResourceProvider


    private val modelAdapter by lazy {
        ModelRecyclerViewAdapter<LectureModel, LectureListViewModel>(
            modelList = listOf(),
            viewModel,
            resourcesProvider = resourceProvider,
            adapterListener = object : AdapterListener {}
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.category = arguments?.getSerializable(CATEGORY) as CollegeCategory
        super.onViewCreated(view, savedInstanceState)
    }


    override fun initViews() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false )
            adapter = modelAdapter
        }

        bindViews()
    }

    private fun bindViews() = with(binding){
        searchEditText.setOnClickListener {

            //todo Activity에 Event를 전송 : SharedViewModel쓰자~?



        }
    }


    override fun observeData() = viewModel.lectureListLiveData.observe(viewLifecycleOwner) { lectureList->
        modelAdapter.submitList(lectureList.map { it.toLectureModel() })
    }


    companion object {
        fun newInstance(category: CollegeCategory) = LectureListFragment().apply {
            arguments = bundleOf(
                CATEGORY to category
            )
        }

        private const val CATEGORY = "category"
    }
}