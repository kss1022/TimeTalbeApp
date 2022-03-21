package com.example.suwon_university_community.ui.main.time_table

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.FragmentTimeTableBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.model.TimeTableCellModel
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.AddTimeTableCellActivity
import javax.inject.Inject

// TODO: 기본적으로 18시까지는 보여준다.  만약 더 추가된다면 어떻게 할것인가???
//  기본 화면에서는 전체를 Scroll뷰로 보여준다.
//  추가화면에서는 Table을 Scroll뷰로 보여준다.


class TimeTableFragment : BaseFragment<TimeTableViewModel, FragmentTimeTableBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: TimeTableViewModel by viewModels<TimeTableViewModel> { viewModelFactory }


    override fun getViewBinding(): FragmentTimeTableBinding =
        FragmentTimeTableBinding.inflate(layoutInflater)

    private val addTimerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "시간표가 추가되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "시간표가 추가되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }


    private var addButtonList: ArrayList<Triple<Long, Int, Int>> =
        arrayListOf<Triple<Long, Int, Int>>()


    override fun initViews() {
        bindViews()
    }


    private fun bindViews() {
        binding.addButton.setOnClickListener {
            addTimerLauncher.launch(
                AddTimeTableCellActivity.newIntent(requireContext() , viewModel.mainTimeTable)
            )
        }

        binding.listButton.setOnClickListener {
            // TODO: 시간표 리스트 보여주기
        }

        binding.addTimeTableButton.setOnClickListener {
            showAlertDialog()
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
    }

    private fun handleSuccessState(timeTableState: TimeTableState.Success) = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.isGone = true

        scrollView.visibility = View.VISIBLE
        noTimeTableTextView.isGone = true
        addTimeTableButton.isGone = true



        addLectureList(
            timeTableState.timeTableWithCell.timeTableCell.map {
            it.toModel()
            }
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
    }



    private fun handleErrorState(timeTableState: TimeTableState.Error) = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.visibility = View.VISIBLE

        scrollView.isGone = true

        errorMessageTextView.text = getString(timeTableState.massageId)
    }


    private fun showAlertDialog() {
        val numberPickers =
            LayoutInflater.from(requireContext()).inflate(R.layout.timetable_date_picker, null)

        val year = getYearNumberPicker(numberPickers)
        val semester = getSemesterNumberPicker(numberPickers)
        val tableName = numberPickers.findViewById<EditText>(R.id.tableNameEditText)



        AlertDialog.Builder(requireContext())
            .setView(numberPickers)
            .setTitle("시간표 만들기")
            .setPositiveButton("OK") { dialog, _ ->


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
            .setNegativeButton("NO") { dialog, _ ->
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
    }


    private fun addLecture(context: Context, model: TimeTableCellModel) {

        model.locationAndTimeList.forEach { locationAndTime ->
            val location = locationAndTime.location
            val day = locationAndTime.day
            val time = locationAndTime.time

            val button = createButton(context, model, location, time)

            addButton(day, button, model)
        }
    }


    private fun createButton(
        context: Context,
        model: TimeTableCellModel,
        location: String,
        time: List<Int>
    ): Button = Button(context).apply {
        setText("${model.name}\n${location}")
        textSize = 10f
        gravity = Gravity.START

        height = ((time.size) * 60).fromDpToPx()
        setPadding(6.fromDpToPx(), 6.fromDpToPx(), 6.fromDpToPx(), 6.fromDpToPx())

        id = ViewCompat.generateViewId()
        setBackgroundResource(R.color.red)

        setOnClickListener { view ->
            val id = addButtonList.filter {
                it.second == view.id
            }.first().first

            addButtonList.filter {
                it.first == id
            }.forEach {
                val view = binding.root.findViewById<FrameLayout>(it.third)
                view.removeView(view.findViewById(it.second))
            }
        }

        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        lp.gravity = Gravity.START
        lp.topMargin = (time[0] * 60 + 29).fromDpToPx()
        lp.marginStart = 2.fromDpToPx()
        lp.marginEnd = 2.fromDpToPx()

        layoutParams = lp
    }


    private fun addButton(
        day: String,
        button: Button,
        model: TimeTableCellModel
    ) {
        when (day) {
            "월" -> {
                binding.monLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.monLinearLayout.id
                    )
                )
            }

            "화" -> {
                binding.tueLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.tueLinearLayout.id
                    )
                )
            }

            "수" -> {
                binding.wedLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.wedLinearLayout.id
                    )
                )
            }

            "목" -> {
                binding.thuLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.thuLinearLayout.id
                    )
                )
            }

            "금" -> {
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


    companion object {
        fun newInstance() = TimeTableFragment()

        const val TAG = "TimeTableFragment"

        private const val DEFAULT_YEAR = 2022
        private const val MIN_YEAR = 2015
    }
}