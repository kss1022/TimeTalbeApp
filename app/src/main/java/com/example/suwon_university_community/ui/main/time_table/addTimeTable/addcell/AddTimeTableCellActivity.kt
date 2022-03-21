package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.data.entity.lecture.LectureEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import com.example.suwon_university_community.databinding.ActivityAddTimeTableCellBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.ui.base.BaseActivity
import javax.inject.Inject

class AddTimeTableCellActivity : BaseActivity<AddTimeTableCellViewModel, ActivityAddTimeTableCellBinding>(){


    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory



    override val viewModel: AddTimeTableCellViewModel by viewModels<AddTimeTableCellViewModel> {
        viewModelFactory
    }
    override fun getViewBinding(): ActivityAddTimeTableCellBinding = ActivityAddTimeTableCellBinding.inflate(layoutInflater)


    private var addButtonList: ArrayList<Triple<Long, Int, Int>> =
        arrayListOf<Triple<Long, Int, Int>>()


    override fun observeData() {

    }

    override fun initViews() {
        binding.toolBar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        val timetableWithCell = intent.getParcelableExtra<TimeTableWithCell>(EXTRA_TIME_TABLE)
        Log.e("Intent", timetableWithCell.toString())


        bindViews()
    }

    private fun bindViews() = with(binding){
        binding.addButton.setOnClickListener {

            addLecture( this@AddTimeTableCellActivity, LectureEntity(
                id = 1,
                name = "강의 추가",
                distinguish = "ㅇㅁㄹ",
                grade = 1,
                time = "인문211(월:1 :1,2)",
                collegeCategory = null,
                department = null,
                professorName = "교수"
            ).toTimeTableCellEntity(2021))
        }
    }



    private fun addLectureList(lectureList: List<LectureEntity>, timeTableId: Int) {

        lectureList.forEach { lecture ->
            addLecture(this, lecture.toTimeTableCellEntity(timeTableId))
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
                addButtonList.add(Triple(timeTableCell.cellId, button.id, binding.monLinearLayout.id))
            }

            "화" -> {
                binding.tueLinearLayout.addView(button)
                addButtonList.add(Triple(timeTableCell.cellId, button.id, binding.tueLinearLayout.id))
            }

            "수" -> {
                binding.wedLinearLayout.addView(button)
                addButtonList.add(Triple(timeTableCell.cellId, button.id, binding.wedLinearLayout.id))
            }

            "목" -> {
                binding.thuLinearLayout.addView(button)
                addButtonList.add(Triple(timeTableCell.cellId, button.id, binding.thuLinearLayout.id))
            }

            "금" -> {
                binding.friLinearLayout.addView(button)
                addButtonList.add(Triple(timeTableCell.cellId, button.id, binding.friLinearLayout.id))
            }

            else -> Unit
        }
    }



    companion object{
        fun newIntent(context : Context , timeTableWithCell: TimeTableWithCell ) = Intent( context, AddTimeTableCellActivity::class.java).apply {
            putExtra( EXTRA_TIME_TABLE, timeTableWithCell)
        }

         const val EXTRA_TIME_TABLE = "time_table"
    }
}