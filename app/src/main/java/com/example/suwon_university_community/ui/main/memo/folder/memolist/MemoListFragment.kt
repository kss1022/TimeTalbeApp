package com.example.suwon_university_community.ui.main.memo.folder.memolist

import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.databinding.FragmentMemoListBinding
import com.example.suwon_university_community.model.MemoModel
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.ModelRecyclerViewAdapter
import com.example.suwon_university_community.widget.adapter.listener.MemoListAdapterListener
import javax.inject.Inject

class MemoListFragment : BaseFragment<MemoListViewModel, FragmentMemoListBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: MemoListViewModel by viewModels { viewModelFactory }

    override fun getViewBinding(): FragmentMemoListBinding =
        FragmentMemoListBinding.inflate(layoutInflater)

    private val arguments: MemoListFragmentArgs by navArgs()


    private lateinit var callback: OnBackPressedCallback


    @Inject
    lateinit var resourceProvider: ResourceProvider


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }


    override fun observeData() = viewModel.memoListStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is MemoListState.Loading -> {
                handleLoadingState()
            }
            is MemoListState.Success -> {
                handleSuccessState(it)
            }

            else -> Unit
        }
    }


    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
        addMemoFloatingButton.isGone = true
        searchEditText.isGone = true
        linearLayout.isGone = true
    }


    private fun handleSuccessState(state: MemoListState.Success) = with((binding)) {
        progressBar.isGone = true
        addMemoFloatingButton.visibility = View.VISIBLE
        searchEditText.visibility = View.VISIBLE
        linearLayout.visibility = View.VISIBLE

        if(state.memoList.isNullOrEmpty().not()){
            (recyclerView.adapter as ModelRecyclerViewAdapter<*,*>).run {
                this.submitList(state.memoList)
            }

            memoTitleTextView.visibility =View.VISIBLE
        }else{
            memoTitleTextView.isGone= true
        }

    }

    override fun initViews() {
        viewModel.folderId = arguments.folderId
        initRecyclerView()
        bindViews()
    }


    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = ModelRecyclerViewAdapter<MemoModel, MemoListViewModel>(
                modelList = listOf(),
                viewModel,
                resourcesProvider = resourceProvider,
                adapterListener = object : MemoListAdapterListener {
                    override fun selectModel(model: MemoModel) {
                        findNavController().navigate(
                            MemoListFragmentDirections.actionMemoListFragmentToEditMemoFragment(
                                model
                            )
                        )
                    }
                }
            )
        }
    }


    private fun bindViews() = with(binding) {
        addMemoFloatingButton.setOnClickListener {
            findNavController().navigate(
                MemoListFragmentDirections.actionMemoListFragmentToEditMemoFragment(
                    MemoModel(
                        id = -1,
                        memoFolderId = arguments.folderId,
                    )
                )
            )
        }
    }
}
