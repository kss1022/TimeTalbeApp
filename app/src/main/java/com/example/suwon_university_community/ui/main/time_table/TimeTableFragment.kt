package com.example.suwon_university_community.ui.main.time_table

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import com.example.suwon_university_community.databinding.FragmentTimeTableBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.model.TimeTableCellModel
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.AddTimeTableCellActivity
import javax.inject.Inject


class TimeTableFragment : BaseFragment<TimeTableViewModel, FragmentTimeTableBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: TimeTableViewModel by viewModels<TimeTableViewModel> { viewModelFactory }


    override fun getViewBinding(): FragmentTimeTableBinding =
        FragmentTimeTableBinding.inflate(layoutInflater)

    private val addTimerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                removeAddedView()
                viewModel.fetchData()
            } else {
                // 새로운 강의가 추가되지 않은 경우
            }
        }

    private lateinit var timeTableWithCell: TimeTableWithCell


    override fun initViews() {
        bindViews()
    }


    private fun bindViews() = with(binding) {
        addButton.setOnClickListener {

            directAddButton.isVisible = directAddButton.isVisible.not()
            searchAddButton.isVisible = searchAddButton.isVisible.not()

            directAddButton.alpha = 0f
            searchAddButton.alpha = 0f

            searchAddButton.animate().alpha(1f).setDuration(100L).setListener(object :
                AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    directAddButton.animate().alpha(1f).setDuration(100L).setListener(null)
                }
            })


        }

        searchAddButton.setOnClickListener {
            addTimerLauncher.launch(
                AddTimeTableCellActivity.newIntent(requireContext(), viewModel.mainTimeTable)
            )
        }


        directAddButton.setOnClickListener {


            TimeTableBottomSheetFragment.newInstance(timeTableWithCell, null) { timeTableCellEntity, i->

                when (i) {
                    TimeTableBottomSheetFragment.NEW -> {
                        timeTableCellEntity?.let {
                            removeAddedView()
                            viewModel.addTimeTableEntity(timeTableCellEntity) }
                    }
                    else -> Unit
                }
            }.show(requireActivity().supportFragmentManager, TimeTableBottomSheetFragment.TAG)
        }


        listButton.setOnClickListener {
            // TODO: 시간표 리스트 보여주기
        }

        addTimeTableButton.setOnClickListener {
            createTimeTableAlertDialog()
        }
    }


    override fun observeData() = viewModel.timeTableStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is TimeTableState.Loading -> {
                handleLoadingState()
            }

            is TimeTableState.NoTable -> {
                handleNoTableState()
            }

            is TimeTableState.Success -> {
                handleSuccessState(it)
            }

            is TimeTableState.Error -> {
                handleErrorState(it)
            }
            else -> Unit
        }
    }


    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
        errorMessageTextView.isGone = true

        scrollView.isGone = true
        addButton.isEnabled = false
        listButton.isEnabled = false
    }


    private fun handleSuccessState(timeTableState: TimeTableState.Success) = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.isGone = true

        scrollView.visibility = View.VISIBLE
        noTimeTableTextView.isGone = true
        addTimeTableButton.isGone = true

        addButton.isEnabled = true
        listButton.isEnabled = true

        timeTableWithCell = timeTableState.timeTableWithCell

        addLectureList(
            timeTableState.timeTableWithCell.timeTableCellList.map {
                it.toModel()
            }
        )


        toolBarTimeTableNameTextView.text =
            if (timeTableState.timeTableWithCell.timeTable.isDefault) getString(R.string.time_table) else timeTableState.timeTableWithCell.timeTable.tableName
        toolBarTimeTableSeasonTextview.text = getString(
            R.string.timetable_season,
            timeTableState.timeTableWithCell.timeTable.year,
            timeTableState.timeTableWithCell.timeTable.semester
        )
    }

    private fun handleNoTableState() = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.isGone = true

        scrollView.visibility = View.VISIBLE
        noTimeTableTextView.visibility = View.VISIBLE
        noTimeTableTextView.text =
            getString(R.string.is_not_exist_timetable_please_create_timetable)

        addTimeTableButton.visibility = View.VISIBLE
        addTimeTableButton.text = getString(R.string.create_new_timetable)

        addButton.isEnabled = false

        toolBarTimeTableNameTextView.text = ""
    }


    private fun handleErrorState(timeTableState: TimeTableState.Error) = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.visibility = View.VISIBLE

        scrollView.isGone = true

        addButton.isEnabled = false
        listButton.isEnabled = false

        errorMessageTextView.text = getString(timeTableState.massageId)
    }


    private fun createTimeTableAlertDialog() {
        val numberPickers =
            LayoutInflater.from(requireContext()).inflate(R.layout.timetable_date_picker, null)

        val year = getYearNumberPicker(numberPickers)
        val semester = getSemesterNumberPicker(numberPickers)
        val tableName = numberPickers.findViewById<EditText>(R.id.tableNameEditText)



        AlertDialog.Builder(requireContext())
            .setView(numberPickers)
            .setTitle("시간표 만들기")
            .setPositiveButton("확인") { dialog, _ ->


                //year.displayedValues[year.value - MIN_YEAR]
                //semester.value
                var timeTableName: String = tableName.text.toString()

                if (timeTableName.isBlank()) {
                    timeTableName =
                        "${year.displayedValues[year.value - MIN_YEAR]} ${semester.value}학기"
                }

                viewModel.saveNewTimeTable(
                    timeTableName,
                    (year.displayedValues[year.value - MIN_YEAR]).substring(0..3).toInt(),
                    semester.value
                )

                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    private fun getYearNumberPicker(numberPickers: View): NumberPicker =
        numberPickers.findViewById<NumberPicker>(R.id.yearNumberPicker).apply {
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            maxValue = DEFAULT_YEAR
            minValue = MIN_YEAR

            val yearDisplayArray = arrayListOf<String>()

            for (i in 0..(DEFAULT_YEAR - MIN_YEAR)) {
                yearDisplayArray.add("${DEFAULT_YEAR - i}년도")
            }

            displayedValues = yearDisplayArray.toArray(arrayOfNulls<String>(yearDisplayArray.size))
        }


    private fun getSemesterNumberPicker(numberPickers: View): NumberPicker =
        numberPickers.findViewById<NumberPicker>(R.id.semesterNumberPicker).apply {
            maxValue = 2
            minValue = 1

            displayedValues = arrayOf(
                "1학기", "2학기"
            )
        }


    private fun addLectureList(timetableCellModelList: List<TimeTableCellModel>) {

        timetableCellModelList.forEach { cell ->
            addLecture(requireContext(), cell)
        }

        checkTimeAndAddView(timetableCellModelList)

    }


    private fun addLecture(context: Context, model: TimeTableCellModel) {

        model.locationAndTimeList.forEach { locationAndTime ->
            val location = locationAndTime.location
            val day = locationAndTime.day.char
            val time = locationAndTime.time


            val button = createButton(context, model, location, time)

            addButton(day, button, model)
        }
    }


    private var addButtonList: ArrayList<Triple<Long, Int, Int>> = arrayListOf()

    private var addedGridViewList: ArrayList<Int> = arrayListOf()
    private var addedTextViewList: ArrayList<Int> = arrayListOf()


    private fun checkTimeAndAddView(timetableCellModelList: List<TimeTableCellModel>) {

        var lastTime = 4


        timetableCellModelList.forEach { timetableCell ->
            timetableCell.locationAndTimeList.forEach {
                val tableLastTime = it.time.second / 60 - 12
                if (tableLastTime > lastTime) lastTime = tableLastTime
            }
        }

        if (lastTime > 4) {
            for (i in 0..lastTime - 5) {
                val gridView = View(requireContext()).apply {

                    setBackgroundResource(R.color.colorPrimary)

                    id = ViewCompat.generateViewId()

                    val lp = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    lp.height = 1.fromDpToPx()


                    layoutParams = lp
                }

                val textView = TextView(requireContext()).apply {
                    width = 20.fromDpToPx()
                    height = 60.fromDpToPx()

                    text = "${5 + i}"

                    id = ViewCompat.generateViewId()

                    gravity = Gravity.END or Gravity.TOP
                    setPadding(0.fromDpToPx(), 0.fromDpToPx(), 3.fromDpToPx(), 0.fromDpToPx())


                    val lp = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    lp.gravity = Gravity.START
                    layoutParams = lp
                }


                binding.leftLinearLayout.addView(gridView)
                binding.leftLinearLayout.addView(textView)

                addedGridViewList.add(gridView.id)
                addedTextViewList.add(textView.id)
            }
        }


    }


    private fun createButton(
        context: Context,
        model: TimeTableCellModel,
        location: String,
        time: Pair<Int, Int>
    ): Button = Button(context).apply {
        text = getString(R.string.timetable_cell_title, model.name, location)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        typeface = Typeface.DEFAULT_BOLD
        textSize = 10f
        gravity = Gravity.START


        setBackgroundColor(ContextCompat.getColor(context, model.cellColor))
        stateListAnimator = null

        setPadding(6.fromDpToPx(), 6.fromDpToPx(), 6.fromDpToPx(), 6.fromDpToPx())

        id = ViewCompat.generateViewId()

        //todo 시간표를 선택하였을 경우AlertDialog를 통해 보여준다. 이떄 수정모드를 눌렀을떄 BottomSheet보여주기
        setOnClickListener { view ->
            val modelId = addButtonList.filter {
                it.second == view.id
            }.first().first

            if (::timeTableWithCell.isInitialized) {
                val clickItem = timeTableWithCell.timeTableCellList.find {
                    it.cellId == modelId
                } ?: return@setOnClickListener

                TimeTableBottomSheetFragment.newInstance(timeTableWithCell, clickItem) { timeTableCellEntity, i->
                    when ( i) {
                        TimeTableBottomSheetFragment.EDIT -> {
                            timeTableCellEntity?.let{
                                removeAddedView()
                                viewModel.updateTimeTableEntity(timeTableCellEntity)
                            }
                        }

                        TimeTableBottomSheetFragment.DELETE -> {
                            removeAddedView()
                            viewModel.deleteTImeTableEntity(clickItem.cellId)
//
//                            addButtonList.filter {
//                                it.first == modelId
//                            }.forEach {
//                                val view = binding.root.findViewById<FrameLayout>(it.third)
//                                view.removeView(view.findViewById(it.second))
//                            }
                        }

                        else -> Unit
                    }
                }.show(requireActivity().supportFragmentManager, TimeTableBottomSheetFragment.TAG)

            }
        }


        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        var minutes = time.second - time.first
        minutes += (minutes / 60)

        var marginTop = (time.first - 540)
        marginTop += marginTop / 60

        lp.height = minutes.fromDpToPx()
        lp.gravity = Gravity.START
        lp.topMargin = marginTop.fromDpToPx()


        layoutParams = lp
    }


    private fun addButton(
        day: Char,
        button: Button,
        model: TimeTableCellModel
    ) {
        when (day) {
            '월' -> {
                binding.monLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.monLinearLayout.id
                    )
                )
            }

            '화' -> {
                binding.tueLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.tueLinearLayout.id
                    )
                )
            }

            '수' -> {
                binding.wedLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.wedLinearLayout.id
                    )
                )
            }

            '목' -> {
                binding.thuLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.thuLinearLayout.id
                    )
                )
            }

            '금' -> {
                binding.friLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.friLinearLayout.id
                    )
                )
            }

            else -> Unit
        }
    }


    private fun removeAddedView() {
        addButtonList.forEach {
            val view = binding.root.findViewById<FrameLayout>(it.third)
            view.removeView(view.findViewById(it.second))
        }


        binding.leftLinearLayout.let { leftLinearLayout ->
            addedGridViewList.forEach {
                leftLinearLayout.removeView(leftLinearLayout.findViewById(it))
            }


            addedTextViewList.forEach {
                leftLinearLayout.removeView(leftLinearLayout.findViewById(it))
            }
        }

        addButtonList.clear()
        addedGridViewList.clear()
        addedTextViewList.clear()
    }


    companion object {
        fun newInstance() = TimeTableFragment()

        const val TAG = "TimeTableFragment"

        private const val DEFAULT_YEAR = 2022
        private const val MIN_YEAR = 2015
    }
}