package net.suwon.plus.ui.main.memo.folder.memolist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.suwon.plus.databinding.FragmentMemoListBinding
import net.suwon.plus.model.MemoModel
import net.suwon.plus.ui.base.BaseFragment
import net.suwon.plus.ui.main.MainActivitySharedViewModel
import net.suwon.plus.ui.main.memo.folder.FolderSelectSheetFragment
import net.suwon.plus.util.SwipeHelperCallback
import net.suwon.plus.util.provider.ResourceProvider
import net.suwon.plus.widget.adapter.ModelRecyclerViewAdapter
import net.suwon.plus.widget.adapter.listener.MemoListAdapterListener
import javax.inject.Inject

class MemoListFragment : BaseFragment<MemoListViewModel, FragmentMemoListBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: MemoListViewModel by viewModels { viewModelFactory }
    private val sharedViewModel: MainActivitySharedViewModel by activityViewModels { viewModelFactory }


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

    override fun onDestroyView() {
        (binding.recyclerView.adapter as ModelRecyclerViewAdapter<*, *>).run {
            this.removeAll()
        }
        super.onDestroyView()
    }


    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }


    override fun observeData() {
        viewModel.memoListStateLiveData.observe(viewLifecycleOwner) {
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

        sharedViewModel.memoUpdateLiveData.observe(viewLifecycleOwner){
            viewModel.fetchData()
        }
    }


    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
        addMemoFloatingButton.isGone = true
        linearLayout.isGone = true
    }


    private fun handleSuccessState(state: MemoListState.Success) = with((binding)) {
        progressBar.isGone = true
        addMemoFloatingButton.visibility = View.VISIBLE
        linearLayout.visibility = View.VISIBLE


        (recyclerView.adapter as ModelRecyclerViewAdapter<*, *>).run {
            this.submitList(state.memoList)
        }
    }

    override fun initViews() {
        viewModel.folderId = arguments.folderId
        initRecyclerView()
        bindViews()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initRecyclerView() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
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

                    override fun selectEdit(model: MemoModel) {
                        showEditBottomSheet(model)
                    }

                    override fun selectDelete(model: MemoModel) {
                        showDeleteAlertDialog(model)
                    }
                }
            )
        }

        val swipeHelperCallback = SwipeHelperCallback()

        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(recyclerView)

        touchView.setOnTouchListener { _, _ ->
            viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                swipeHelperCallback.removePreviousClamp(recyclerView)
            }
            false
        }

        addMemoFloatingButton.setOnTouchListener { _, _ ->
            viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                swipeHelperCallback.removePreviousClamp(recyclerView)
            }
            false
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


    private fun showDeleteAlertDialog(model: MemoModel) {
        AlertDialog.Builder(requireContext())
            .setMessage("선택한 메모를 삭제하기겠습니까?")
            .setPositiveButton("확인") { dialog, _ ->
                viewModel.deleteMemo(model)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditBottomSheet(model: MemoModel) {
        FolderSelectSheetFragment.newInstance(arguments.folderId) {
            if (model.memoFolderId != it) {
                viewModel.changeFolder(model, it)
            }
        }.show(
            requireActivity().supportFragmentManager,
            FolderSelectSheetFragment.TAG
        )
    }
}
