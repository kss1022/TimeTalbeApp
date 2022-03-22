package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.data.entity.lecture.CollegeCategory
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import com.example.suwon_university_community.databinding.ActivityAddTimeTableCellBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.model.TimeTableCellModel
import com.example.suwon_university_community.ui.base.BaseActivity
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.lecturelist.LectureListFragment
import com.example.suwon_university_community.widget.adapter.LectureListFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject


class AddTimeTableCellActivity :
    BaseActivity<AddTimeTableCellViewModel, ActivityAddTimeTableCellBinding>() {


    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewPagerAdapter: LectureListFragmentAdapter

    override val viewModel: AddTimeTableCellViewModel by viewModels<AddTimeTableCellViewModel> {
        viewModelFactory
    }

    override fun getViewBinding(): ActivityAddTimeTableCellBinding =
        ActivityAddTimeTableCellBinding.inflate(layoutInflater)


    private var addButtonList: ArrayList<Triple<Long, Int, Int>> =
        arrayListOf<Triple<Long, Int, Int>>()


    override fun observeData() {

    }

    override fun initViews() {

        val timetableWithCell = intent.getParcelableExtra<TimeTableWithCell>(EXTRA_TIME_TABLE)

        initTimeTable(timetableWithCell)
        initViewPager()
        bindViews()
    }

    private fun initTimeTable(timetableWithCell: TimeTableWithCell?) {
        timetableWithCell?.let { timetableWithCell ->
            if (timetableWithCell.timeTableCellList.isNullOrEmpty().not()) {
                addLectureList(
                    timetableWithCell.timeTableCellList.map { it.toModel() }
                )
            }
        }

    }

    private fun initViewPager() = with(binding) {


        val collegeCategories = CollegeCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            val collegeList = collegeCategories.map {
                LectureListFragment.newInstance(it)
            }

            viewPagerAdapter = LectureListFragmentAdapter(
                this@AddTimeTableCellActivity,
                collegeList
            )

            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = collegeCategories.size

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(collegeCategories[position].categoryNameId)
            }.attach()
        }


    }

    private fun bindViews() = with(binding) {



        toolBar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }


        addButton.setOnClickListener {
        }
    }


    private fun addLectureList(modelList: List<TimeTableCellModel>) {
        modelList.forEach { model ->
            addLecture(this, model)
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


    companion object {
        fun newIntent(context: Context, timeTableWithCell: TimeTableWithCell) =
            Intent(context, AddTimeTableCellActivity::class.java).apply {
                putExtra(EXTRA_TIME_TABLE, timeTableWithCell)
            }

        const val EXTRA_TIME_TABLE = "time_table"
    }
}