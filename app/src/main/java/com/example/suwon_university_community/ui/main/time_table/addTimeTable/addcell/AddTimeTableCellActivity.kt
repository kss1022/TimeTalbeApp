package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.suwon_university_community.R
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

    private val sharedViewModel: AddTimeTableCellSharedViewModel by viewModels()

    private lateinit var viewPagerAdapter: LectureListFragmentAdapter

    override val viewModel: AddTimeTableCellViewModel by viewModels<AddTimeTableCellViewModel> {
        viewModelFactory
    }


    override fun getViewBinding(): ActivityAddTimeTableCellBinding =
        ActivityAddTimeTableCellBinding.inflate(layoutInflater)


    private var isMotionEnd = false
    private var currentPage = 0

    private val viewPagerOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Log.e("ViewPager" , "Selected")

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


    override fun observeData() {
        sharedViewModel.lectureEntityLiveData.observe(this) {
            if(isMotionEnd){
                binding.motionLayout.transitionToStart()
            }

            val cellModel = it.toTimeTableCellModel()
            addLecture(this, cellModel)


            binding.scrollView.scrollY =
                ((cellModel.locationAndTimeList[0].time[0]) * 60).fromDpToPx()
        }
    }

    override fun initViews() {

        val timetableWithCell = intent.getParcelableExtra<TimeTableWithCell>(EXTRA_TIME_TABLE)

        initTimeTable(timetableWithCell)
        initViewPager()
        bindViews()


        binding.gradeSpinner.adapter = ArrayAdapter.createFromResource(
            this@AddTimeTableCellActivity,
            R.array.year_spinner,
            android.R.layout.simple_spinner_item
        )
    }

    private fun initTimeTable(timetableWithCell: TimeTableWithCell?) {
        timetableWithCell?.let { timetableWithCell ->
            if (timetableWithCell.timeTableCellList.isNullOrEmpty().not()) {
                timetableWithCell.timeTableCellList.map { it.toModel() }.forEach { model ->
                    addLecture(this, model)
                }
            }
        }

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
            setResult(Activity.RESULT_OK)
            finish()
        }


        addButton.setOnClickListener {
            if(isMotionEnd){
                motionLayout.transitionToStart()
            }else{
                motionLayout.transitionToEnd()
            }
        }

        searchEditText.apply {
            addTextChangedListener {
                viewPagerAdapter.fragmentList.forEach {
                    it.viewModel.setSearchString(searchEditText.text.toString())
                }
            }

            setOnClickListener {
                if (isMotionEnd.not()) {
                    motionLayout.transitionToEnd()
                }
            }

            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus && isMotionEnd.not()){
                    motionLayout.transitionToEnd()
                }
            }
        }


        bindMotionLayout()


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
                    viewPagerAdapter.fragmentList[currentPage].viewModel.setDepartmentString(parent?.getItemAtPosition(pos).toString())
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
                viewPagerAdapter.fragmentList[currentPage].viewModel.setMajorString(parent?.getItemAtPosition(pos).toString(), pos)
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
                viewPagerAdapter.fragmentList[currentPage].viewModel.setGradeString(parent?.getItemAtPosition(pos).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
        }

    }


    private fun bindMotionLayout() {
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                if (isMotionEnd) hideSoftKeyboard()
            }

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
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(viewPagerOnPageChangeCallback)
    }



    /**
     * Spinner
     */


    private fun getDepartmentList(): List<String> {
        return if (currentPage == 0) {
            val departmentList = arrayListOf<String>()

            departmentList.add( resources.getString( CollegeCategory.values()[0].categoryNameId))

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
            if(pos == 0){
                majorList.add(getString(CollegeCategory.values()[currentPage].categoryTypeList[pos].first))
                return majorList
            }


            var college = 1
            var collegeDepartment = pos

            while (true) {
                var currentCount = 0

                currentCount = CollegeCategory.values()[college].categoryTypeList.size - 1

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


    private fun hideSoftKeyboard() {
        val inputManger =
            this@AddTimeTableCellActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManger.hideSoftInputFromWindow(
            this@AddTimeTableCellActivity.currentFocus?.windowToken,
            0
        )
    }


    companion object {
        fun newIntent(context: Context, timeTableWithCell: TimeTableWithCell) =
            Intent(context, AddTimeTableCellActivity::class.java).apply {
                putExtra(EXTRA_TIME_TABLE, timeTableWithCell)
            }

        const val EXTRA_TIME_TABLE = "time_table"
    }
}