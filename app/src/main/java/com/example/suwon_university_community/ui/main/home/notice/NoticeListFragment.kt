package com.example.suwon_university_community.ui.main.home.notice

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.databinding.FragmentNoticeListBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.NoticeAdapter
import javax.inject.Inject

class NoticeListFragment : BaseFragment<NoticeViewModel, FragmentNoticeListBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: NoticeViewModel by viewModels { viewModelFactory }

    override fun getViewBinding(): FragmentNoticeListBinding =
        FragmentNoticeListBinding.inflate(layoutInflater)

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.category = arguments?.getSerializable(CATEGORY) as NoticeCategory
        super.onViewCreated(view, savedInstanceState)
    }


    override fun observeData() = viewModel.noticeListStateLiveData.observe(viewLifecycleOwner) {
        when (it) {

            is NoticeListState.Loading -> {
            }
            is NoticeListState.Success -> {
                handleSuccessState(it)
            }

            is NoticeListState.Error -> {
            }
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun handleSuccessState(it: NoticeListState.Success) {
        (binding.recyclerView.adapter as? NoticeAdapter)?.run {
            addData(it.noticeDateModelList)
            notifyDataSetChanged()
        }
    }

    override fun initViews() {
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
        (binding.recyclerView.adapter as  NoticeAdapter)?.apply {
            onItemClickListener ={ noticeModel ->
                val url = Uri.parse(noticeModel.url)
                CustomTabsIntent.Builder().build().also {
                    it.launchUrl(requireContext(), url)
                }
            }
        }
    }


    companion object {
        fun newInstance(category: NoticeCategory) = NoticeListFragment().apply {
            arguments = bundleOf(
                CATEGORY to category
            )
        }

        private const val CATEGORY = "CATEGORY"
    }
}