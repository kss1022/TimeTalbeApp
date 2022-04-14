package com.example.suwon_university_community.ui.main.memo.folder.timetablememolist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import com.example.suwon_university_community.databinding.FragmentTimeTableMemoListBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.model.MemoModel
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.ui.main.memo.folder.memolist.MemoListFragmentArgs
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.ModelRecyclerViewAdapter
import com.example.suwon_university_community.widget.adapter.listener.MemoListAdapterListener
import javax.inject.Inject

class TimeTableMemoListFragment :
    BaseFragment<TimeTableMemoListViewModel, FragmentTimeTableMemoListBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: TimeTableMemoListViewModel by viewModels { viewModelFactory }


    override fun getViewBinding(): FragmentTimeTableMemoListBinding =
        FragmentTimeTableMemoListBinding.inflate(layoutInflater)


    private lateinit var callback: OnBackPressedCallback
    private val arguments: MemoListFragmentArgs by navArgs()


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


    override fun observeData() =
        viewModel.timetableMemoListStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TimeTableMemoListState.Loading -> {
                    handleLoadingState()
                }

                is TimeTableMemoListState.Success -> {
                    handleSuccessState(it)
                }

                is TimeTableMemoListState.NoTimeTable -> {
                    handleNoTimeTableState(it)
                }
                else -> Unit
            }
        }


    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
        linearLayout.isGone = true
        addMemoFloatingButton.isGone = true
        searchEditText.isGone = true
    }


    private fun handleSuccessState(state: TimeTableMemoListState.Success) = with(binding) {
        progressBar.isGone = true
        linearLayout.visibility = View.VISIBLE
        addMemoFloatingButton.visibility = View.VISIBLE
        searchEditText.visibility = View.VISIBLE

        timeTableNameTextView.text =
            state.timeTableWithCell.timeTable.tableName.ifEmpty { getString(R.string.time_table) }
        timeTableSeasonTextView.text = getString(
            R.string.timetable_season,
            state.timeTableWithCell.timeTable.year,
            state.timeTableWithCell.timeTable.semester
        )

        createTimeTableCellList(state.timeTableWithCell.timeTableCellList)
        submitMemoList(state.memoList)
    }


    private fun handleNoTimeTableState(state: TimeTableMemoListState.NoTimeTable) = with(binding) {
        progressBar.isGone = true
        showSelectTimeTableAlertDialog(state.timeTableList)
    }


    override fun initViews() {
        viewModel.folderId = arguments.folderId
    }


    private val adapterList =
        arrayListOf<Pair<ModelRecyclerViewAdapter<MemoModel, TimeTableMemoListViewModel>, Long>>()

    @SuppressLint("InflateParams")
    private fun createTimeTableCellList(timeTableCellList: List<TimeTableCellEntity>) {
        val downArrow = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_keyboard_arrow_right_24
        )

        val rightArrow = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_keyboard_arrow_down_24
        )

        timeTableCellList.forEach { cell ->
            val titleTextView = TextView(requireContext()).apply {

                text = cell.name
                textSize = 14f
                setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    downArrow,
                    null
                )

                setPadding(0, 14.fromDpToPx(), 0, 14.fromDpToPx())

                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams = lp
            }


            val memoRecyclerView = RecyclerView(requireContext()).apply {
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams = lp

                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter = createModelAdapter(cell.cellId)
            }


            titleTextView.setOnClickListener {
                if (memoRecyclerView.isVisible) {
                    memoRecyclerView.isGone = true
                    titleTextView.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        downArrow,
                        null
                    )
                } else {
                    memoRecyclerView.visibility = View.VISIBLE
                    titleTextView.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        rightArrow,
                        null
                    )
                }
            }


            binding.linearLayout.addView(titleTextView)
            binding.linearLayout.addView(memoRecyclerView)
        }
    }

    private fun createModelAdapter(cellId: Long): ModelRecyclerViewAdapter<MemoModel, TimeTableMemoListViewModel> {

        val adapter = ModelRecyclerViewAdapter<MemoModel, TimeTableMemoListViewModel>(
            modelList = listOf(),
            viewModel,
            resourcesProvider = resourceProvider,
            adapterListener = object : MemoListAdapterListener {
                override fun selectModel(model: MemoModel) {
                    findNavController().navigate(R.id.editMemoFragment)
                }
            }
        )

        adapterList.add(adapter to cellId)

        return adapter
    }


    private fun submitMemoList(memoList: List<MemoModel>) {
        adapterList.forEach { pair ->
            val filterByIdList = memoList.filter { memo ->
                memo.timeTableCellId == pair.second
            }

            pair.first.submitList(filterByIdList)
        }
    }


    private fun showSelectTimeTableAlertDialog(timeTableList: List<TimeTableEntity>) {

        if (timeTableList.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.is_not_exist_timetable_please_create_timetable),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
            return
        }

        val timeTableListNumberPicker = createTimeTableNumberPicker(timeTableList)
        val container = FrameLayout(requireContext()).apply {
            addView(timeTableListNumberPicker)
        }
        AlertDialog.Builder(requireContext())
            .setMessage("가져올 시간표를 선택해주세요!")
            .setView(container)
            .setPositiveButton("확인") { dialog, _ ->
                val selectPosition = timeTableListNumberPicker.value
                viewModel.updateFolderWithTimeTable(timeTableList[selectPosition].tableId)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
                findNavController().popBackStack()
            }
            .setOnCancelListener {
                findNavController().popBackStack()
            }
            .show()
    }


    private fun createTimeTableNumberPicker(timeTableList: List<TimeTableEntity>): NumberPicker =
        NumberPicker(requireContext()).apply {
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            maxValue = timeTableList.size - 1
            minValue = 0

            val timeTableNameArray = arrayListOf<String>()
            timeTableList.forEach {
                timeTableNameArray.add("${it.year}년도 ${it.semester}학기 ${it.tableName}")
            }
            displayedValues =
                timeTableNameArray.toArray(arrayOfNulls<String>(timeTableNameArray.size))

            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lp.gravity = Gravity.CENTER
            layoutParams = lp
        }
}