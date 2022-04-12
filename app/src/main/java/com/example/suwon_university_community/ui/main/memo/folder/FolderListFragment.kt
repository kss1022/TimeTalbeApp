package com.example.suwon_university_community.ui.main.memo.folder

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.memo.FolderCategory
import com.example.suwon_university_community.databinding.FragmentFolderListBinding
import com.example.suwon_university_community.model.FolderModel
import com.example.suwon_university_community.model.LectureModel
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.ModelRecyclerViewAdapter
import com.example.suwon_university_community.widget.adapter.listener.FolderListAdapterListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class FolderListFragment : BaseFragment<FolderListViewModel, FragmentFolderListBinding>() {

    @Inject
    override lateinit var  viewModelFactory: ViewModelProvider.Factory

    override val viewModel: FolderListViewModel by viewModels {  viewModelFactory }
    override fun getViewBinding(): FragmentFolderListBinding  = FragmentFolderListBinding.inflate(layoutInflater)

    @Inject
    lateinit var resourceProvider : ResourceProvider

    private val noticeModelAdapter by lazy {
        ModelRecyclerViewAdapter<LectureModel, FolderListViewModel>(
            modelList = listOf(),
            viewModel,
            resourcesProvider = resourceProvider,
            adapterListener = object : FolderListAdapterListener {
                override fun selectFolder(model: FolderModel) {
                    Log.e("FolderCLick", model.toString())
                }
            }
        )
    }


    private val timeTableModelAdapter by lazy {
        ModelRecyclerViewAdapter<LectureModel, FolderListViewModel>(
            modelList = listOf(),
            viewModel,
            resourcesProvider = resourceProvider,
            adapterListener = object : FolderListAdapterListener {
                override fun selectFolder(model: FolderModel) {
                    Log.e("FolderCLick", model.toString())
                }
            }
        )
    }


    override fun observeData() {
        viewModel.memoStateLiveData.observe(viewLifecycleOwner) {
            when (it) {

                is FolderListState.Loading -> {
                    handleLoadingState()
                }
                is FolderListState.Success -> {
                    handleSuccessState()
                }

                else-> Unit
            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.folders.collect(){ folderList->
                val noticeFolder = folderList.filter { it.category == FolderCategory.NOTICE }.map{
                    FolderModel(
                        id=  it.folderId,
                        name = it.name,
                        count =  it.count,
                        category = it.category,
                        isDefault = it.isDefault
                    )
                }
                val timeTableFolder = folderList.filter { it.category == FolderCategory.TIME_TABLE }.map{
                    FolderModel(
                        id=  it.folderId,
                        name = it.name,
                        count =  it.count,
                        category = it.category,
                        isDefault = it.isDefault
                    )
                }

                if(noticeFolder.isNullOrEmpty().not()){
                    noticeModelAdapter.submitList(noticeFolder)

                }

                if(timeTableFolder.isNullOrEmpty().not()){
                    timeTableModelAdapter.submitList(timeTableFolder)
                }
            }
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
        folderContainer.isGone = true
    }

    private fun handleSuccessState() = with(binding) {
        progressBar.isGone = true
        folderContainer.visibility= View.VISIBLE
    }



    override fun initViews() {
        initRecyclerView()
        bindViews()
    }


    private fun initRecyclerView() = with(binding){
        noticeMemoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = noticeModelAdapter
        }

        timeTableMemoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = timeTableModelAdapter
        }
    }

    private fun bindViews() = with(binding) {
        noticeTextView.setOnClickListener {
            if (noticeMemoContainer.isVisible) {
                noticeMemoContainer.isGone = true
                val downArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
                noticeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, downArrow, null)
            } else {
                noticeMemoContainer.visibility = View.VISIBLE
                val rightArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_down_24
                )
                noticeTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rightArrow, null)
            }
        }



        timeTableTextView.setOnClickListener {
            if (timeTableMemoContainer.isVisible) {
                timeTableMemoContainer.isGone = true
                val downArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
                timeTableTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    downArrow,
                    null
                )
            } else {
                timeTableMemoContainer.visibility = View.VISIBLE
                val rightArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_down_24
                )
                timeTableTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    rightArrow,
                    null
                )
            }
        }

    }
}