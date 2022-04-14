package com.example.suwon_university_community.ui.main.memo.folder.bookmark

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.databinding.FragmentBookmarkListBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.NoticeAdapter
import javax.inject.Inject

class BookmarkListFragment : BaseFragment<BookmarkListViewModel, FragmentBookmarkListBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: BookmarkListViewModel by viewModels { viewModelFactory }

    override fun getViewBinding(): FragmentBookmarkListBinding =
        FragmentBookmarkListBinding.inflate(layoutInflater)

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private val arguments: BookmarkListFragmentArgs by navArgs()

    private lateinit var callback: OnBackPressedCallback


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


    override fun observeData() = viewModel.bookmarkListStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is BookmarkListState.Loading -> {
                handleLoadingState()
            }
            is BookmarkListState.Success -> {
                handleSuccessState(it)
            }

            else -> Unit
        }
    }

    private fun handleLoadingState() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleSuccessState(state: BookmarkListState.Success) = with(binding) {
        if(state.noticeList.isNullOrEmpty()){
            messageTextView.visibility = View.VISIBLE
        }else{
            (recyclerView.adapter as? NoticeAdapter)?.run {
                messageTextView.isGone = true
                addData(state.noticeList)
                notifyDataSetChanged()
            }
        }
    }


    override fun initViews() {
        viewModel.folderId = arguments.folderId
        initRecyclerView()
        bindViews()
    }


    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = NoticeAdapter(resourceProvider)
        }
    }

    private fun bindViews() {
        (binding.recyclerView.adapter as NoticeAdapter).apply {
            onItemClickListener = { noticeModel ->
                val url = Uri.parse(noticeModel.url)
                CustomTabsIntent.Builder().build().also {
                    it.launchUrl(requireContext(), url)
                }
            }
        }
    }



}