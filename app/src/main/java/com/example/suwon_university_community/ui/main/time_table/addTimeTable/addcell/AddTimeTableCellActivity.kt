package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.SystemClock
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.lecture.CollegeCategory
import com.example.suwon_university_community.data.entity.timetable.DayOfTheWeek
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import com.example.suwon_university_community.databinding.ActivityAddTimeTableCellBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.model.LectureModel
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

    private val sharedViewModel: AddTimeTableCellSharedViewModel by viewModels()

    private lateinit var viewPagerAdapter: LectureListFragmentAdapter

    override val viewModel: AddTimeTableCellViewModel by viewModels<AddTimeTableCellViewModel> {
        viewModelFactory
    }


    override fun getViewBinding(): ActivityAddTimeTableCellBinding =
        ActivityAddTimeTableCellBinding.inflate(layoutInflater)


    lateinit var timetableWithCell: TimeTableWithCell


    private var isMotionEnd = false
    private var currentPage = 0

    private val viewPagerOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {

            if (::viewPagerAdapter.isInitialized) {
                currentPage = position

                binding.departmentSpinner.adapter = ArrayAdapter(
                    this@AddTimeTableCellActivity,
                    android.R.layout.simple_spinner_item,
                    getDepartmentList()
                )

                binding.departmentSpinner.setSelection(0)
                binding.majorSpinner.setSelection(0)
                binding.gradeSpinner.setSelection(0)
            }
        }
    }


    override fun initViews() {
        checkTimeTableWithCell()

        initTimeTable()
        initViewPager()
        bindViews()

        binding.gradeSpinner.adapter = ArrayAdapter.createFromResource(
            this@AddTimeTableCellActivity,
            R.array.year_spinner,
            android.R.layout.simple_spinner_item
        )
    }


    override fun observeData() {
        sharedViewModel.lectureEntityLiveData.observe(this) {
            showAlertDialog(it)
        }
    }


    private fun checkTimeTableWithCell() {
        val timetable = intent.getParcelableExtra<TimeTableWithCell>(EXTRA_TIME_TABLE) ?: {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        timetableWithCell = timetable as TimeTableWithCell
    }

    private fun initTimeTable() {
        timetableWithCell.let { timetableWithCell ->
            if (timetableWithCell.timeTableCellList.isNullOrEmpty().not()) {
                val modelList = timetableWithCell.timeTableCellList.map { it.toModel() }
                checkTimeAndAddGridView(modelList)
                modelList.forEach { model ->
                    addLectureOnView(this, model)
                }
            }
        }

        binding.toolBarTimeTableNameTextView.text =
            if (timetableWithCell.timeTable.tableName.isEmpty()) getString(R.string.time_table) else timetableWithCell.timeTable.tableName
        binding.toolBarTimeTableSeasonTextview.text = getString(
            R.string.timetable_season,
            timetableWithCell.timeTable.year,
            timetableWithCell.timeTable.semester
        )
    }

    private fun initViewPager() = with(binding) {

        viewPager.registerOnPageChangeCallback(viewPagerOnPageChangeCallback)

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
            hideSoftKeyboard()
            setResult(Activity.RESULT_OK)
            finish()
        }


        searchEditText.apply {
            inputType = InputType.TYPE_NULL

            var currentClickTime: Long
            var lastClickTime: Long = 0

            addTextChangedListener {
                currentClickTime = SystemClock.uptimeMillis()
                val elapsedTime: Long = currentClickTime - lastClickTime
                lastClickTime = currentClickTime

                if (elapsedTime > 50) {
                    viewPagerAdapter.fragmentList.forEach {
                        it.viewModel.setSearchString(searchEditText.text.toString())
                    }
                }
            }

            setOnClickListener {
                if (isMotionEnd.not()) {
                    inputType = InputType.TYPE_NULL
                    motionLayout.transitionToEnd()
                } else {
                    inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                    showSoftKeyboard()
                }
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && isMotionEnd.not()) {
                    inputType = InputType.TYPE_NULL
                    motionLayout.transitionToEnd()
                } else {
                    inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                    showSoftKeyboard()
                }
            }
        }


        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) = Unit

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) = Unit

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                isMotionEnd = (currentId == R.id.end)
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) = Unit
        })

        binding.departmentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    binding.majorSpinner.adapter = ArrayAdapter(
                        this@AddTimeTableCellActivity,
                        android.R.layout.simple_spinner_item,
                        getMajorList(pos)
                    )
                    viewPagerAdapter.fragmentList[currentPage].viewModel.setDepartmentString(
                        parent?.getItemAtPosition(
                            pos
                        ).toString()
                    )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) = Unit
            }


        binding.majorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                viewPagerAdapter.fragmentList[currentPage].viewModel.setMajorString(
                    parent?.getItemAtPosition(
                        pos
                    ).toString(), pos
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
        }


        binding.gradeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                viewPagerAdapter.fragmentList[currentPage].viewModel.setGradeString(
                    parent?.getItemAtPosition(
                        pos
                    ).toString()
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(viewPagerOnPageChangeCallback)
    }


    private fun showAlertDialog(lectureModel: LectureModel) {
        hideSoftKeyboard()

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.please_check_selected_lecture))
            .setMessage(
                getString(
                    R.string.lecture_info,
                    lectureModel.name ?: "",
                    lectureModel.time ?: "",
                    lectureModel.professorName ?: ""
                )
            )
            .setPositiveButton("네!") { dialog, _ ->
                if (isMotionEnd) {
                    binding.motionLayout.transitionToStart()
                }

                val cellModel = lectureModel.toTimeTableCellModel()

                viewModel.addLecture(
                    timetableWithCell.timeTable.tableId,
                    lectureModel,
                    cellModel.locationAndTimeList
                )

                if (cellModel.locationAndTimeList.isNullOrEmpty().not()) {
                    addLectureOnView(this, cellModel)

                    binding.scrollView.scrollY =
                        (cellModel.locationAndTimeList[0].time.first - 540).fromDpToPx()
                }

                dialog.dismiss()
            }
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }


    /**
     * Spinner
     */


    private fun getDepartmentList(): List<String> {
        return if (currentPage == 0) {
            val departmentList = arrayListOf<String>()

            departmentList.add(resources.getString(CollegeCategory.values()[0].categoryNameId))

            CollegeCategory.values().forEach { collegeCategory ->
                for (i in 1 until collegeCategory.categoryTypeList.size) {
                    departmentList.add(resources.getString(collegeCategory.categoryTypeList[i].first))
                }
            }
            return departmentList
        } else {
            CollegeCategory.values()[currentPage].categoryTypeList.map {
                resources.getString(it.first)
            }
        }
    }

    private fun getMajorList(pos: Int): List<String> {

        val majorList = arrayListOf<String>()
        if (currentPage == 0) {
            if (pos == 0) {
                majorList.add(getString(CollegeCategory.values()[currentPage].categoryTypeList[pos].first))
                return majorList
            }


            var college = 1
            var collegeDepartment = pos

            while (true) {
                val currentCount = CollegeCategory.values()[college].categoryTypeList.size - 1

                if (collegeDepartment - currentCount <= 0) {
                    break
                }

                college++
                collegeDepartment -= currentCount
            }

            majorList.add(getString(CollegeCategory.values()[college].categoryTypeList[collegeDepartment].first))
            CollegeCategory.values()[college].categoryTypeList[collegeDepartment].second.forEach {
                majorList.add(resources.getString(it))
            }

            return majorList
        }


        val currentCategory = CollegeCategory.values()[currentPage]
        majorList.add(getString(currentCategory.categoryTypeList[pos].first))


        if (pos == 0) {
            currentCategory.categoryTypeList.forEach { pair: Pair<Int, List<Int>> ->
                pair.second.forEach {
                    majorList.add(resources.getString(it))
                }
            }
        } else {
            currentCategory.categoryTypeList[pos].second.forEach {
                majorList.add(resources.getString(it))
            }
        }

        return majorList
    }


    /**
     * AddLecture CreateButton -> AddButton ScrollView
     */

    private var addButtonList: ArrayList<Triple<Long, Int, Int>> =
        arrayListOf<Triple<Long, Int, Int>>()

    var minTime = DEFAULT_MIN_TIME

    private fun addLectureOnView(context: Context, model: TimeTableCellModel) {

        model.locationAndTimeList.forEach { locationAndTime ->
            val location = locationAndTime.location
            val day = locationAndTime.day
            val time = locationAndTime.time


            val button = createButton(context, model, location, time)

            addButton(day, button, model)
        }
    }


    private fun checkTimeAndAddGridView(timetableCellModelList: List<TimeTableCellModel>) {
        var maxTime = DEFAULT_MAX_TIME
        minTime = DEFAULT_MIN_TIME

        timetableCellModelList.forEach { timetableCell ->
            timetableCell.locationAndTimeList.forEach {
                if (it.day != DayOfTheWeek.DEFAULT) {
                    val tableMaxTime = it.time.second / 60
                    if (tableMaxTime > maxTime) maxTime = tableMaxTime

                    val tableMinTime = it.time.first / 60
                    if (tableMinTime < minTime) minTime = tableMinTime
                }
            }
        }

        if (maxTime > DEFAULT_MAX_TIME) {
            for (i in (DEFAULT_MAX_TIME + 1)..maxTime) {
                addGrid(i)
            }
        }

        if (minTime < DEFAULT_MIN_TIME) {
            for (i in (DEFAULT_MIN_TIME - 1) downTo minTime) {
                addGrid(i)
            }
        }

    }

    private fun addGrid(i: Int) {
        val gridView = View(this).apply {

            setBackgroundResource(R.color.colorPrimary)

            id = ViewCompat.generateViewId()

            val lp = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            lp.height = 1.fromDpToPx()


            layoutParams = lp
        }

        val textView = TextView(this).apply {
            width = 20.fromDpToPx()
            height = 60.fromDpToPx()

            text = if (i > DEFAULT_MAX_TIME) "${i - 12}" else "$i"

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



        if (i > DEFAULT_MAX_TIME) {
            binding.leftLinearLayout.addView(gridView)
            binding.leftLinearLayout.addView(textView)
        } else {
            binding.leftLinearLayout.addView(textView, 0)
            binding.leftLinearLayout.addView(gridView, 0)
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

//        setOnClickListener { view ->
//            val id = addButtonList.filter {
//                it.second == view.id
//            }.first().first
//
//            addButtonList.filter {
//                it.first == id
//            }.forEach {
//                val dayFrameLayout = binding.root.findViewById<FrameLayout>(it.third)
//                dayFrameLayout.removeView(dayFrameLayout.findViewById(it.second))
//            }
//        }

        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        var minutes = time.second - time.first
        minutes += (minutes / 60)

        var marginTop = (time.first - minTime * 60)
        marginTop += marginTop / 60


        lp.height = minutes.fromDpToPx()
        lp.gravity = Gravity.START
        lp.topMargin = marginTop.fromDpToPx()

        layoutParams = lp
    }


    private fun addButton(
        day: DayOfTheWeek,
        button: Button,
        model: TimeTableCellModel
    ) {
        when (day) {
            DayOfTheWeek.MON -> {
                binding.monLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.monLinearLayout.id
                    )
                )
            }

            DayOfTheWeek.TUE -> {
                binding.tueLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.tueLinearLayout.id
                    )
                )
            }

            DayOfTheWeek.WED -> {
                binding.wedLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.wedLinearLayout.id
                    )
                )
            }

            DayOfTheWeek.THU -> {
                binding.thuLinearLayout.addView(button)
                addButtonList.add(
                    Triple(
                        model.id,
                        button.id,
                        binding.thuLinearLayout.id
                    )
                )
            }

            DayOfTheWeek.FRI -> {
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


    private fun hideSoftKeyboard() {
        val inputManger =
            this@AddTimeTableCellActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManger.hideSoftInputFromWindow(
            this@AddTimeTableCellActivity.currentFocus?.windowToken,
            0
        )
    }


    private fun showSoftKeyboard() {
        val inputManger =
            this@AddTimeTableCellActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManger.showSoftInput(
            binding.searchEditText, InputMethodManager.RESULT_SHOWN
        )
    }


    companion object {
        fun newIntent(context: Context, timeTableWithCell: TimeTableWithCell) =
            Intent(context, AddTimeTableCellActivity::class.java).apply {
                putExtra(EXTRA_TIME_TABLE, timeTableWithCell)
            }

        const val EXTRA_TIME_TABLE = "time_table"

        private const val DEFAULT_MAX_TIME = 22
        private const val DEFAULT_MIN_TIME = 9
    }
}