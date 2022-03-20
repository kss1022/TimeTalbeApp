package com.example.suwon_university_community.ui.main.time_table

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
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
import com.example.suwon_university_community.data.entity.lecture.LectureEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.databinding.FragmentTimeTableBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.AddTimeTableActivity
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
                AddTimeTableActivity.newIntent(requireContext())
            )
        }

        binding.listButton.setOnClickListener {
            // TODO: 시간표 리스트 보여주기
        }

        binding.addTimeTableButton.setOnClickListener {
            showAlertDialog()
        }
    }

    // TODO:  EditText도 추가하여 시간표 이름을 받는다.
    //  setOnValueChangedListener 를 통해 값을 받아서 EditText를 설정해준다.


    private fun showAlertDialog() {
        val numberPickers =
            LayoutInflater.from(requireContext()).inflate(R.layout.timetable_date_picker, null)

        val defaultYear = 2022
        val minYear = 2015

        val year = numberPickers.findViewById<NumberPicker>(R.id.yearNumberPicker).apply {
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            maxValue = defaultYear
            minValue = minYear
            
            val yearDisplayArray = arrayListOf<String>()
            
            for( i in 0 .. (defaultYear -minYear) ) {
                yearDisplayArray.add( "${defaultYear-i}년도")
            }

            displayedValues = yearDisplayArray.toArray(arrayOfNulls<String>(yearDisplayArray.size))
        }
        val semester = numberPickers.findViewById<NumberPicker>(R.id.semesterNumberPicker).apply {
            maxValue = 2
            minValue = 1
            
            displayedValues = arrayOf(
                "1학기" , "2학기"
            )
        }

        AlertDialog.Builder(requireContext())
            .setView(numberPickers)
            .setPositiveButton("OK") { dialog, _ ->
                Log.e("yearNumberPicker",
                    year.displayedValues[year.value - minYear]
                )
                Log.e("semesterNumberPicker", "${semester.value}")
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
    
    

    override fun observeData() = viewModel.timeTableStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is TimeTableState.Loading -> {
                handleLoadingState()
            }

            is TimeTableState.Success -> {
                handleSuccessState()
            }

            is TimeTableState.NoTable -> {
                handleNoTableState()
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

    private fun handleSuccessState() = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.isGone = true

        scrollView.visibility = View.VISIBLE
        noTimeTableTextView.isGone = true
        addTimeTableButton.isGone = true
    }

    private fun handleNoTableState() = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.isGone = true

        scrollView.visibility = View.VISIBLE
        noTimeTableTextView.visibility = View.VISIBLE
        addTimeTableButton.visibility = View.VISIBLE
    }


    private fun handleErrorState(timeTableState: TimeTableState.Error) = with(binding) {
        progressBar.isGone = true
        errorMessageTextView.visibility = View.VISIBLE

        scrollView.isGone = true

        errorMessageTextView.text = getString(timeTableState.massageId)
    }


    private fun addLectureList(lectureList: List<LectureEntity>, timeTableId: Int) {

        lectureList.forEach { lecture ->
            addLecture(requireContext(), lecture.toTimeTableCellEntity(timeTableId))
        }
    }


    private fun addLecture(context: Context, lecture: TimeTableCellEntity) {

        lecture.locationAndTimeList.forEach { locationAndTime ->
            val location = locationAndTime.location
            val day = locationAndTime.day
            val time = locationAndTime.time

            val button = createButton(context, lecture, location, time)

            addButton(day, button, lecture)
        }
    }


    private fun createButton(
        context: Context,
        timeTableCell: TimeTableCellEntity,
        location: String,
        time: List<Int>
    ): Button = Button(context).apply {
        setText("${timeTableCell.name}\n${location}")
        textSize = 10f
        gravity = Gravity.START

        height = ((time.size) * 60).fromDpToPx()
        setPadding(6.fromDpToPx(), 6.fromDpToPx(), 6.fromDpToPx(), 6.fromDpToPx())

        id = ViewCompat.generateViewId()

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

        layoutParams = lp
    }


    private fun addButton(
        day: String,
        button: Button,
        timeTableCell: TimeTableCellEntity
    ) {
        when (day) {
            "월" -> {
                binding.monLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        timeTableCell.cellId,
                        button.id,
                        binding.monLinearLayout.id
                    )
                )
            }

            "화" -> {
                binding.tueLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        timeTableCell.cellId,
                        button.id,
                        binding.tueLinearLayout.id
                    )
                )
            }

            "수" -> {
                binding.wedLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        timeTableCell.cellId,
                        button.id,
                        binding.wedLinearLayout.id
                    )
                )
            }

            "목" -> {
                binding.thuLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        timeTableCell.cellId,
                        button.id,
                        binding.thuLinearLayout.id
                    )
                )
            }

            "금" -> {
                binding.friLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        timeTableCell.cellId,
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
    }
}