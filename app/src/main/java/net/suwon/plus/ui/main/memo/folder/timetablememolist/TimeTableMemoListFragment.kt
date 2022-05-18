package net.suwon.plus.ui.main.memo.folder.timetablememolist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipDescription
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.DragEvent
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
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
import net.suwon.plus.R
import net.suwon.plus.data.entity.timetable.TimeTableCellEntity
import net.suwon.plus.data.entity.timetable.TimeTableEntity
import net.suwon.plus.databinding.FragmentTimeTableMemoListBinding
import net.suwon.plus.extensions.fromDpToPx
import net.suwon.plus.model.MemoModel
import net.suwon.plus.ui.base.BaseFragment
import net.suwon.plus.ui.main.MainActivitySharedViewModel
import net.suwon.plus.ui.main.memo.folder.FolderSelectSheetFragment
import net.suwon.plus.ui.main.memo.folder.memolist.MemoListFragmentArgs
import net.suwon.plus.util.SwipeHelperCallback
import net.suwon.plus.util.provider.ResourceProvider
import net.suwon.plus.widget.adapter.ModelRecyclerViewAdapter
import net.suwon.plus.widget.adapter.listener.MemoListAdapterListener
import javax.inject.Inject


class TimeTableMemoListFragment :
    BaseFragment<TimeTableMemoListViewModel, FragmentTimeTableMemoListBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: TimeTableMemoListViewModel by viewModels { viewModelFactory }
    private val sharedViewModel: MainActivitySharedViewModel by activityViewModels { viewModelFactory }


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

    override fun onDestroyView() {
        super.onDestroyView()
        adapterList.clear()
        swipeHelperList.clear()
    }

    override fun observeData() {
        viewModel.timetableMemoListStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TimeTableMemoListState.Loading -> {
                    handleLoadingState()
                }

                is TimeTableMemoListState.Success -> {
                    handleSuccessState(it)
                }

                is TimeTableMemoListState.EditMemo -> {
                    handleEditState(it)
                }

                is TimeTableMemoListState.NoTimeTable -> {
                    handleNoTimeTableState(it)
                }
                else -> Unit
            }
        }


        sharedViewModel.memoUpdateLiveData.observe(viewLifecycleOwner){
            it?.let {
                if(viewModel.timetableMemoListStateLiveData.value is TimeTableMemoListState.Success){
                    viewModel.updateMemo(it)
                }
            }
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
        addMemoFloatingButton.isGone = true
        linearLayout.isGone = true
    }


    @SuppressLint("SetTextI18n")
    private fun handleSuccessState(state: TimeTableMemoListState.Success) = with(binding) {
        progressBar.isGone = true
        addMemoFloatingButton.visibility = View.VISIBLE
        linearLayout.visibility = View.VISIBLE

        createTimeTableCellList(state.timeTableWithCell.timeTableCellList)
        submitMemoList(state.memoList.sortedByDescending { it.time })
    }


    private fun handleEditState(state: TimeTableMemoListState.EditMemo) {
        submitMemoList(state.memoList)
    }

    private fun handleNoTimeTableState(state: TimeTableMemoListState.NoTimeTable) = with(binding) {
        progressBar.isGone = true
        showSelectTimeTableAlertDialog(state.timeTableList)
    }


    override fun initViews() {
        viewModel.folderId = arguments.folderId
        initBaseMemoRecyclerView()
        bindViews()
    }


    private fun initBaseMemoRecyclerView() = with(binding) {
        memoTitleTextView.apply {
            getDragListener(null)

            setOnClickListener {
                if (baseMemoRecyclerView.isVisible) {
                    baseMemoRecyclerView.isGone = true
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_keyboard_arrow_right_24
                        ),
                        null
                    )
                } else {
                    baseMemoRecyclerView.visibility = View.VISIBLE
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_keyboard_arrow_down_24
                        ),
                        null
                    )
                }
            }
        }

        baseMemoRecyclerView.apply {

        }

        baseMemoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

            adapter = ModelRecyclerViewAdapter<MemoModel, TimeTableMemoListViewModel>(
                modelList = listOf(),
                viewModel,
                resourcesProvider = resourceProvider,
                adapterListener = object : MemoListAdapterListener {
                    override fun selectModel(model: MemoModel) {
                        findNavController().navigate(
                            TimeTableMemoListFragmentDirections.actionTimeTableMemoListFragmentToEditMemoFragment(
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

            getDragListener(null)
        }

        val swipeHelperCallback = SwipeHelperCallback()

        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(baseMemoRecyclerView)


        swipeHelperList.add(swipeHelperCallback to baseMemoRecyclerView)
    }


    private fun bindViews() = with(binding) {
        addMemoFloatingButton.setOnClickListener {

            findNavController().navigate(
                TimeTableMemoListFragmentDirections.actionTimeTableMemoListFragmentToEditMemoFragment(
                    MemoModel(
                        id = -1,
                        memoFolderId = arguments.folderId,
                    )
                )
            )
        }

    }


    /**
     *  AddView
     */

    private val adapterList =
        arrayListOf<Pair<ModelRecyclerViewAdapter<MemoModel, TimeTableMemoListViewModel>, Long>>()

    private val swipeHelperList = arrayListOf<Pair<SwipeHelperCallback, RecyclerView>>()

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    private fun createTimeTableCellList(timeTableCellList: List<TimeTableCellEntity>) =
        with(binding) {

            val downArrow = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_keyboard_arrow_down_24
            )

            val rightArrow = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_keyboard_arrow_right_24
            )

            timeTableCellList.forEach { cell ->

//            if(adapterList.find { it.second == cell.cellId } != null) return@forEach


                val titleTextView = getTitleTextView(cell, downArrow)
                val memoRecyclerView = getMemoRecyclerView(cell)


                titleTextView.also {
                    it.getDragListener(cell)
                    it.setOnClickListener {
                        if (memoRecyclerView.isVisible) {
                            memoRecyclerView.isGone = true
                            titleTextView.setCompoundDrawablesWithIntrinsicBounds(
                                null,
                                null,
                                rightArrow,
                                null
                            )
                        } else {
                            memoRecyclerView.visibility = View.VISIBLE
                            titleTextView.setCompoundDrawablesWithIntrinsicBounds(
                                null,
                                null,
                                downArrow,
                                null
                            )
                        }
                    }
                }



                memoRecyclerView.getDragListener(cell)

                linearLayout.addView(memoRecyclerView, 0)
                linearLayout.addView(titleTextView, 0)
            }

            touchView.setOnTouchListener { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(100)
                    swipeHelperList.forEach {
                        it.first.removePreviousClamp(it.second)
                    }
                }

                false
            }

            addMemoFloatingButton.setOnTouchListener { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(100)
                    swipeHelperList.forEach {
                        it.first.removePreviousClamp(it.second)
                    }
                }
                false
            }
        }


    private fun getTitleTextView(
        cell: TimeTableCellEntity,
        downArrow: Drawable?
    ): TextView =
        TextView(requireContext()).apply {

            text = cell.name
            textSize = 12f


            setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                downArrow,
                null

            )

            setPadding(16.fromDpToPx(), 6.fromDpToPx(), 16.fromDpToPx(), 3.fromDpToPx())

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams = lp

        }


    private fun getMemoRecyclerView(cell: TimeTableCellEntity): RecyclerView {
        val recyclerView = RecyclerView(requireContext()).apply {
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams = lp

            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

            adapter = createModelAdapter(cell.cellId)

            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimaryVariant
                )
            )
        }

        val swipeHelperCallback = SwipeHelperCallback()
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(recyclerView)

        swipeHelperList.add(swipeHelperCallback to recyclerView)

        return recyclerView
    }

    private fun createModelAdapter(cellId: Long): ModelRecyclerViewAdapter<MemoModel, TimeTableMemoListViewModel> {

        val adapter = ModelRecyclerViewAdapter<MemoModel, TimeTableMemoListViewModel>(
            modelList = listOf(),
            viewModel,
            resourcesProvider = resourceProvider,
            adapterListener = object : MemoListAdapterListener {
                override fun selectModel(model: MemoModel) {
                    findNavController().navigate(
                        TimeTableMemoListFragmentDirections.actionTimeTableMemoListFragmentToEditMemoFragment(
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

        adapterList.add(adapter to cellId)

        return adapter
    }


    private fun View.getDragListener(cell: TimeTableCellEntity?) {
        setOnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    (view as? RecyclerView)?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    (view as? TextView)?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimaryVariant
                        )
                    )

                    view?.invalidate()
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> true

                DragEvent.ACTION_DRAG_EXITED -> {
                    (view as? RecyclerView)?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimaryVariant
                        )
                    )
                    (view as? TextView)?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )

                    view?.invalidate()
                    true
                }

                DragEvent.ACTION_DROP -> {


                    val item = event.clipData.getItemAt(0) ?: return@setOnDragListener false

                    val dragData = item.text?.toString() ?: return@setOnDragListener false
                    viewModel.replaceMemo(dragData.toLong(), cell?.cellId)

                    (view as? RecyclerView)?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimaryVariant
                        )
                    )
                    (view as? TextView)?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    view?.invalidate()

                    //val v = event.localState as ConstraintLayout
                    //val owner = v.parent as ViewGroup
                    //owner.removeView(v)
                    //val destination = view as LinearLayout
                    //destination.addView(v)
                    //v.visibility = View.VISIBLE
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    (view as? RecyclerView)?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimaryVariant
                        )
                    )
                    (view as? TextView)?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )

                    view?.invalidate()
                    true
                }

                else -> false
            }
        }
    }


    private fun submitMemoList(memoList: List<MemoModel>) {
        adapterList.forEach { pair ->
            val filterByIdList = memoList.filter { memo ->
                memo.timeTableCellId == pair.second
            }

            pair.first.submitList(filterByIdList)
        }

        val baseMemoList = memoList.filter { it.timeTableCellId == null }


        (binding.baseMemoRecyclerView.adapter as ModelRecyclerViewAdapter<*, *>).run {
            this.submitList(baseMemoList)
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


    /**
     * SetTimeTable
     */

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