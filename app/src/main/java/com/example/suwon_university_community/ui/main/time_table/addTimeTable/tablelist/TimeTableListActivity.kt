package com.example.suwon_university_community.ui.main.time_table.addTimeTable.tablelist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import com.example.suwon_university_community.databinding.ActivityTimeTableListBinding
import com.example.suwon_university_community.model.LectureModel
import com.example.suwon_university_community.model.TimeTableModel
import com.example.suwon_university_community.ui.base.BaseActivity
import com.example.suwon_university_community.ui.main.time_table.TimeTableFragment
import com.example.suwon_university_community.util.provider.DefaultResourceProvider
import com.example.suwon_university_community.widget.adapter.ModelRecyclerViewAdapter
import com.example.suwon_university_community.widget.adapter.listener.TimeTableListAdapterListener
import javax.inject.Inject


class TimeTableListActivity : BaseActivity<TimeTableListViewModel, ActivityTimeTableListBinding>() {


    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: TimeTableListViewModel by viewModels {
        viewModelFactory
    }

    override fun getViewBinding(): ActivityTimeTableListBinding =
        ActivityTimeTableListBinding.inflate(layoutInflater)

    private val timeTableWithCell: TimeTableWithCell? by lazy {
        intent.extras?.getParcelable(EXTRA_TABLE_KEY)
    }

    @Inject
    lateinit var resourceProvider: DefaultResourceProvider

    private val modelAdapter by lazy {
        ModelRecyclerViewAdapter<LectureModel, TimeTableListViewModel>(
            modelList = listOf(),
            viewModel,
            resourcesProvider = resourceProvider,
            adapterListener = object : TimeTableListAdapterListener {
                override fun changeTimeTable(timeTableModel: TimeTableModel) {
                    viewModel.changeMainTableId(timeTableModel.id)
                    setResult(Activity.RESULT_OK)
                    finish()
                }

                override fun editTimeTable(timeTableModel: TimeTableModel) {
                    editTimeTableAlertDialog(timeTableModel)
                }

                override fun deleteTimeTable(timeTableModel: TimeTableModel) {
                    if (timeTableModel.id == timeTableWithCell?.timeTable?.tableId) {
                        Toast.makeText(
                            this@TimeTableListActivity,
                            "현재 보고 있는 테이블을 삭제할 수 없습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    deleteAlertDialog(timeTableModel.id)
                }
            }
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (timeTableWithCell == null) finish()
    }

    override fun initViews(){
        initRecyclerView()
        initMainTable()
        bindViews()
    }

    private fun initRecyclerView(){
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@TimeTableListActivity, RecyclerView.VERTICAL, false)
            adapter = modelAdapter
        }
    }



    private fun bindViews() = with(binding) {
        toolBar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }


        addButton.setOnClickListener {
            timeTableAlertDialog()
        }

        cardContainer.setOnClickListener {
            finish()
        }

        mainTableEditButton.setOnClickListener {
            editTimeTableAlertDialog(timeTableWithCell!!.timeTable.toModel())
        }
    }


    override fun observeData() = viewModel.timeTableListStateLiveData.observe(this) {
        when (it) {
            is TimeTableListState.Loading -> {
                handleLoadingState()
            }
            is TimeTableListState.Success -> {
                handleSuccessState(it)
            }

            else -> Unit
        }
    }


    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
    }

    private fun handleSuccessState(timeTableListState: TimeTableListState.Success) = with(binding) {
        progressBar.isGone = true
        val timeTableEntityList =
            timeTableListState.timeTableModelList.filter { it.id != timeTableWithCell?.timeTable?.tableId }

        modelAdapter.submitList(timeTableEntityList)
    }


    private fun initMainTable() = with(binding) {
        val timeTable = timeTableWithCell?.timeTable!!
        val lectureList = timeTableWithCell?.timeTableCellList!!

        var time = 0
        lectureList.forEach {
            it.locationAndTimeList.forEach { locationAndTime ->
                time += (locationAndTime.time.second - locationAndTime.time.first)
            }
        }

        val hour = time / 60

        seasonTextView.text =
            getString(R.string.timetable_season, timeTable.year, timeTable.semester)
        tableNameTextView.text =
            if (timeTable.tableName.isEmpty()) getString(R.string.time_table) else timeTable.tableName

        lectureCountTextView.text = lectureList.size.toString()
        lectureTotalTimeTextView.text = if (hour < 1) "${time}분" else "${hour}시간 ${time - hour * 60}분"
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }



    private fun timeTableAlertDialog() {
        val numberPickers =
            LayoutInflater.from(this).inflate(R.layout.timetable_date_picker, null)

        val year = getYearNumberPicker(numberPickers)
        val semester = getSemesterNumberPicker(numberPickers)
        val tableName = numberPickers.findViewById<EditText>(R.id.tableNameEditText)

        AlertDialog.Builder(this)
            .setView(numberPickers)
            .setTitle("시간표 만들기")
            .setPositiveButton("확인") { dialog, _ ->
                viewModel.saveNewTimeTable(
                    tableName.text.toString(),
                    (year.displayedValues[year.value - TimeTableFragment.MIN_YEAR]).substring(0..3)
                        .toInt(),
                    semester.value
                )

                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }


    private fun editTimeTableAlertDialog(timeTableModel: TimeTableModel) {
        val numberPickers =
            LayoutInflater.from(this).inflate(R.layout.timetable_date_picker, null)

        val year = getYearNumberPicker(numberPickers, timeTableModel.year)
        val semester = getSemesterNumberPicker(numberPickers, timeTableModel.semester)
        val tableName = numberPickers.findViewById<EditText>(R.id.tableNameEditText).apply {
            setText(timeTableModel.tableName)
        }

        AlertDialog.Builder(this)
            .setView(numberPickers)
            .setTitle("시간표 수정하기")
            .setPositiveButton("확인") { dialog, _ ->
                viewModel.editTimeTable(
                    timeTableModel.id,
                    tableName.text.toString(),
                    (year.displayedValues[year.value - TimeTableFragment.MIN_YEAR]).substring(0..3)
                        .toInt(),
                    semester.value,
                    timeTableModel.isDefault
                )

                if(timeTableModel.id == timeTableWithCell?.timeTable?.tableId){
                    binding.seasonTextView.text =
                        getString(R.string.timetable_season,  (year.displayedValues[year.value - TimeTableFragment.MIN_YEAR]).substring(0..3)
                            .toInt(), semester.value)
                    binding.tableNameTextView.text =
                        if (tableName.text.toString().isEmpty()) getString(R.string.time_table) else tableName.text.toString()
                }

                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }


    private fun deleteAlertDialog(timeTableId: Long) {
        AlertDialog.Builder(this)
            .setTitle("정말 삭제하시겠어요?")
            .setPositiveButton("네") { dialog, _ ->
                viewModel.deleteTimeTable(timeTableId)
                dialog.dismiss()
            }
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    private fun getYearNumberPicker(numberPickers: View, year: Int? = null): NumberPicker =
        numberPickers.findViewById<NumberPicker>(R.id.yearNumberPicker).apply {
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            maxValue = TimeTableFragment.DEFAULT_YEAR
            minValue = TimeTableFragment.MIN_YEAR

            val yearDisplayArray = arrayListOf<String>()

            for (i in 0..(TimeTableFragment.DEFAULT_YEAR - TimeTableFragment.MIN_YEAR)) {
                yearDisplayArray.add("${TimeTableFragment.DEFAULT_YEAR - i}년도")
            }

            displayedValues = yearDisplayArray.toArray(arrayOfNulls<String>(yearDisplayArray.size))

            year?.let { value = minValue + (maxValue - it) }
        }


    private fun getSemesterNumberPicker(numberPickers: View, semester: Int? = null): NumberPicker =
        numberPickers.findViewById<NumberPicker>(R.id.semesterNumberPicker).apply {
            maxValue = 2
            minValue = 1

            displayedValues = arrayOf(
                "1학기", "2학기"
            )

            semester?.let { value = it }
        }

    companion object {
        fun newInstance(context: Context, timeTableWithCell: TimeTableWithCell) =
            Intent(context, TimeTableListActivity::class.java).apply {
                putExtra(EXTRA_TABLE_KEY, timeTableWithCell)
            }

        private const val EXTRA_TABLE_KEY = "MainTable"
    }
}