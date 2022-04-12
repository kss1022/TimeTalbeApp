package com.example.suwon_university_community.ui.main.home.notice

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.databinding.FragmentNoticeListBinding
import com.example.suwon_university_community.model.NoticeModel
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
                handleLoadingState()
            }
            is NoticeListState.Success -> {
                handleSuccessState(it)
            }

            is NoticeListState.Error -> {
                handleErrorState(it)
            }

            else->Unit
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
        errorMessageTextView.isGone = true
        recyclerView.isGone = true
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun handleSuccessState(it: NoticeListState.Success) = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.isGone = true
        recyclerView.visibility = View.VISIBLE


        (recyclerView.adapter as? NoticeAdapter)?.run {
            addData(it.noticeDateModelList)
            notifyDataSetChanged()
        }
    }

    private fun handleErrorState(it: NoticeListState.Error) = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.visibility = View.VISIBLE
        recyclerView.isGone = true

        errorMessageTextView.text = getString(it.massageId)
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
        (binding.recyclerView.adapter as NoticeAdapter).apply {
            onItemClickListener = { noticeModel ->
                val url = Uri.parse(noticeModel.url)
                CustomTabsIntent.Builder().build().also {
                    it.launchUrl(requireContext(), url)
                }
            }

            onItemLongClickListener = { noticeModel ->
                showBookMarkAlertDialog(noticeModel)
            }
        }
    }

    private fun showBookMarkAlertDialog(noticeModel: NoticeModel) {
        AlertDialog.Builder(requireContext())
            .setMessage("해당 공지를 북마크에 추가하시겠습니까?")
            .setPositiveButton("확인"){ dialog, _ ->
                viewModel.saveNotice(noticeModel)
                dialog.dismiss()
            }.setNegativeButton("취소"){ dialog, _ ->
                dialog.dismiss()
            }.show()
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