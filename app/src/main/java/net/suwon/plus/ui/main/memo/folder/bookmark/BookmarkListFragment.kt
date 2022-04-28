package net.suwon.plus.ui.main.memo.folder.bookmark

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.suwon.plus.databinding.FragmentBookmarkListBinding
import net.suwon.plus.model.NoticeDateModel
import net.suwon.plus.model.NoticeModel
import net.suwon.plus.ui.base.BaseFragment
import net.suwon.plus.util.provider.ResourceProvider
import net.suwon.plus.widget.adapter.CustomAdapter.NoticeAdapter
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


    override fun observeData() {
        viewModel.bookmarkListStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is BookmarkListState.Loading -> {
                    handleLoadingState()
                }
                is BookmarkListState.Success -> {
                    handleSuccessState()
                }

                else -> Unit
            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.bookmarks.collect { bookMarkList ->
                if (bookMarkList.isNullOrEmpty()) {
                    binding.messageTextView.visibility = View.VISIBLE
                }


                (binding.recyclerView.adapter as? NoticeAdapter)?.run {
                    binding.messageTextView.isGone = true
                    addData(toNoticeDateModel(bookMarkList.map { it.toModel() }))
                }
            }
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
    }


    private fun handleSuccessState() = with(binding) {
        progressBar.isGone = true
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

            onItemLongClickListener = { noticeModel ->
                showBookMarkAlertDialog(noticeModel)
            }

        }
    }

    private fun showBookMarkAlertDialog(noticeModel: NoticeModel) {
        AlertDialog.Builder(requireContext())
            .setMessage("해당 공지를 북마크에서 제거하시겠습니까?")
            .setPositiveButton("확인"){ dialog, _ ->
                viewModel.deleteBookmark(noticeModel)
                dialog.dismiss()
            }.setNegativeButton("취소"){ dialog, _ ->
                dialog.dismiss()
            }.show()
    }




    private fun toNoticeDateModel(noticeModelList: List<NoticeModel>): MutableList<NoticeDateModel> {
        val noticeDateModel = mutableListOf<NoticeDateModel>()

        val dateSet = mutableSetOf<Triple<Int, Int, Int>>()
        noticeModelList.forEach {
            dateSet.add(it.date)
        }

        val sortedData = dateSet.sortedWith(
            compareByDescending<Triple<Int, Int, Int>> { it.first }
                .thenByDescending { it.second }
                .thenByDescending { it.third }
        )

        sortedData.forEach { sortedDate ->
            val noticeList = mutableListOf<NoticeModel>()

            noticeModelList.forEach { noticeDate ->
                if (sortedDate == noticeDate.date) {
                    noticeList.add(noticeDate)
                }
            }

            noticeDateModel.add(
                NoticeDateModel(
                    sortedDate,
                    noticeList.sortedBy { it.category })
            )
        }
        return noticeDateModel
    }


}