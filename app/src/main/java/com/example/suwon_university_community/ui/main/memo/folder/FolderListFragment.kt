package com.example.suwon_university_community.ui.main.memo.folder

import android.app.AlertDialog
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.memo.FolderCategory
import com.example.suwon_university_community.data.entity.memo.FolderEntity
import com.example.suwon_university_community.databinding.FragmentFolderListBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.model.FolderModel
import com.example.suwon_university_community.model.LectureModel
import com.example.suwon_university_community.ui.base.BaseFragment
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.ModelRecyclerViewAdapter
import com.example.suwon_university_community.widget.adapter.listener.FolderListAdapterListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

// todo 시간표에 기본 폴더를 추가해주지 않고(기본 메모 폴더를 2번으로변경)
//  폴더가 없는경우 -> 폴더를 추가하라는 text와 버튼을 보여준다.
//  시간표가 추가되지 않은경우 시간표를 추가하라는 text를 보여준다.
class FolderListFragment : BaseFragment<FolderListViewModel, FragmentFolderListBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: FolderListViewModel by viewModels { viewModelFactory }
    override fun getViewBinding(): FragmentFolderListBinding =
        FragmentFolderListBinding.inflate(layoutInflater)

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private val noticeFolderModelAdapter by lazy {
        ModelRecyclerViewAdapter<LectureModel, FolderListViewModel>(
            modelList = listOf(),
            viewModel,
            resourcesProvider = resourceProvider,
            adapterListener = object : FolderListAdapterListener {
                override fun selectFolder(model: FolderModel) {
                    findNavController().navigate(
                        FolderListFragmentDirections.actionFolderListFragmentToBookMarkListFragment(
                            model.id,
                            model.name
                        )
                    )
                }
            }
        )
    }


    private val timeTableFolderModelAdapter by lazy {
        ModelRecyclerViewAdapter<LectureModel, FolderListViewModel>(
            modelList = listOf(),
            viewModel,
            resourcesProvider = resourceProvider,
            adapterListener = object : FolderListAdapterListener {
                override fun selectFolder(model: FolderModel) {
                    viewModel.checkTimeTableCount(model)
                }
            }
        )
    }


    private val memoFolderAdapter by lazy {
        ModelRecyclerViewAdapter<LectureModel, FolderListViewModel>(
            modelList = listOf(),
            viewModel,
            resourcesProvider = resourceProvider,
            adapterListener = object : FolderListAdapterListener {
                override fun selectFolder(model: FolderModel) {
                    findNavController().navigate(
                        FolderListFragmentDirections.actionFolderListFragmentToMemoListFragment(
                            model.id,
                            model.name
                        )
                    )
                }
            }
        )
    }


    private var clicked = false
    private var timeTableFolderExist = true

    private val rotateAnimationOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.button_rotate_open
        )
    }
    private val rotateAnimationClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.button_rotate_close
        )
    }
    private val slideAnimationUp: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.button_slide_up
        )
    }
    private val slideAnimationDown: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.button_slide_down
        )
    }


    override fun observeData() {
        viewModel.memoStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is FolderListState.Loading -> {
                    handleLoadingState()
                }
                is FolderListState.Success -> {
                    handleSuccessState()
                }

                else -> Unit
            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.folders.collect() { folderList ->
                if (folderList.isNullOrEmpty()) return@collect

                val noticeFolder = folderFilterByCategory(folderList, FolderCategory.NOTICE)
                val timeTableFolder = folderFilterByCategory(folderList, FolderCategory.TIME_TABLE)
                val memoFolder = folderFilterByCategory(folderList, FolderCategory.MEMO)


                if (noticeFolder.isNullOrEmpty().not()) {
                    noticeFolderModelAdapter.submitList(noticeFolder)
                }

                if (timeTableFolder.isNullOrEmpty().not()) {
                    timeTableFolderModelAdapter.submitList(timeTableFolder)
                    if (timeTableFolder.size == 1) {
                        timeTableFolderExist = false
                        binding.timeTableMessageTextView.visibility = View.VISIBLE
                    } else {
                        timeTableFolderExist = true
                        binding.timeTableMessageTextView.isGone = true
                    }
                }

                if (memoFolder.isNullOrEmpty().not()) {
                    memoFolderAdapter.submitList(memoFolder)
                }
            }
        }


        viewModel.timeTableCountLiveData.observe(viewLifecycleOwner) {
            it.first?.let { model ->
                if (it.second > 0) {
                    findNavController().navigate(
                        FolderListFragmentDirections.actionFolderListFragmentToTimeTableMemoListFragment(
                            model.id,
                            model.name
                        )
                    )
                    viewModel.timeTableCountLiveData.value = null to -1
                } else if (it.second == 0) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.is_not_exist_timetable_please_create_timetable),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun handleLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
        folderContainer.isGone = true
    }

    private fun handleSuccessState() = with(binding) {
        progressBar.isGone = true
        folderContainer.visibility = View.VISIBLE
    }


    override fun initViews() = with(binding) {
        initRecyclerView()
        bindViews()
    }


    private fun initRecyclerView() = with(binding) {
        noticeMemoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = noticeFolderModelAdapter
        }

        timeTableMemoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = timeTableFolderModelAdapter
        }


        memoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = memoFolderAdapter
        }

    }

    private fun bindViews() = with(binding) {
        //TitleText
        noticeTitleSetOnClickListener()
        timeTableTitleSetOnClickListener()
        memoTitleSetOnClickListener()

        //FloatingButton
        addFloatingButtonSetOnClickListener()
        addFolderFloatingButtonSetOnClickListener()
        addMemoFloatingButtonSetOnCLickListener()


        addFolderToSelectTitleSetOnClickListener()
    }


    override fun onStart() {
        super.onStart()
        clicked = false
    }


    private fun folderFilterByCategory(
        folderList: List<FolderEntity>,
        folderCategory: FolderCategory
    ) =
        folderList.filter { it.category == folderCategory }.map {
            FolderModel(
                id = it.folderId,
                name = it.name,
                count = it.count,
                category = it.category,
                isDefault = it.isDefault
            )
        }


    private fun noticeTitleSetOnClickListener() = with(binding) {
        noticeTitleTextView.setOnClickListener {
            if (noticeMemoRecyclerView.isVisible) {
                noticeMemoRecyclerView.isGone = true
                val downArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
                noticeTitleTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    downArrow,
                    null
                )
            } else {
                noticeMemoRecyclerView.visibility = View.VISIBLE
                val rightArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_down_24
                )
                noticeTitleTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    rightArrow,
                    null
                )
            }
        }
    }

    private fun timeTableTitleSetOnClickListener() = with(binding) {
        timeTableTitleTextView.setOnClickListener {
            if (timeTableMemoRecyclerView.isVisible) {
                timeTableMemoRecyclerView.isGone = true
                if (!timeTableFolderExist) timeTableMessageTextView.isGone = true
                val downArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
                timeTableTitleTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    downArrow,
                    null
                )
            } else {
                timeTableMemoRecyclerView.visibility = View.VISIBLE
                if (!timeTableFolderExist) timeTableMessageTextView.visibility = View.VISIBLE
                val rightArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_down_24
                )
                timeTableTitleTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    rightArrow,
                    null
                )
            }
        }
    }


    private fun memoTitleSetOnClickListener() = with(binding) {
        memoTitleTextView.setOnClickListener {
            if (memoRecyclerView.isVisible) {
                memoRecyclerView.isGone = true
                val downArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
                memoTitleTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    downArrow,
                    null
                )
            } else {
                memoRecyclerView.visibility = View.VISIBLE
                val rightArrow = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_down_24
                )
                memoTitleTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    rightArrow,
                    null
                )
            }
        }
    }


    private fun addFloatingButtonSetOnClickListener() = with(binding) {
        addFloatingButton.setOnClickListener {
            if (!clicked) {
                showAddFloatingButtons()
            } else {
                hideAddFloatingButtons()
            }
        }
    }


    private fun addFolderFloatingButtonSetOnClickListener() = with(binding) {
        addFolderFloatingButton.setOnClickListener {
            hideAddFloatingButtons()
            addFloatingButton.setImageResource(R.drawable.ic_outline_folder_24)
            addFloatingButton.alpha = 0.2f
            addFloatingButton.isEnabled = false
            addFolderToSelectTitleCardView.visibility = View.VISIBLE
            addFolderBackgroundTouchView.visibility = View.VISIBLE
        }

        addFolderBackgroundTouchView.setOnClickListener {
            hideAddFolderToSelectTitleCardView()
        }
    }


    private fun addMemoFloatingButtonSetOnCLickListener() = with(binding) {
        addMemoFloatingButton.setOnClickListener {
            //todo 메모
            findNavController().navigate(R.id.editMemoFragment)
            hideAddFloatingButtons()
        }
    }


    private fun addFolderToSelectTitleSetOnClickListener() = with(binding) {
        selectTimeTableTitleTextView.setOnClickListener {
            hideAddFolderToSelectTitleCardView()
            showAddFolderAlertDialog(FolderCategory.TIME_TABLE)
        }
        selectMemoTitleTextView.setOnClickListener {
            hideAddFolderToSelectTitleCardView()
            showAddFolderAlertDialog(FolderCategory.MEMO)
        }
    }

    private fun showAddFolderAlertDialog(folderCategory: FolderCategory) {

        val editText = EditText(requireContext()).apply {
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(30.fromDpToPx(), 0, 30.fromDpToPx(), 0)
            layoutParams = lp
        }

        val container = FrameLayout(requireContext()).apply {
            addView(editText)
        }


        AlertDialog.Builder(requireContext())
            .setMessage("생성할 폴더의 이름을 입력해주세요")
            .setView(container)
            .setPositiveButton("확인") { dialog, _ ->
                viewModel.addFolder(editText.text.toString(), folderCategory)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun showAddFloatingButtons() = with(binding) {
        addMemoFloatingButton.visibility = View.VISIBLE
        addFolderFloatingButton.visibility = View.VISIBLE

        addMemoFloatingButton.startAnimation(slideAnimationUp)
        addFolderFloatingButton.startAnimation(slideAnimationUp)
        addFloatingButton.startAnimation(rotateAnimationOpen)
        clicked = !clicked
    }


    private fun hideAddFloatingButtons() = with(binding) {
        addMemoFloatingButton.isGone = true
        addFolderFloatingButton.isGone = true

        addMemoFloatingButton.startAnimation(slideAnimationDown)
        addFolderFloatingButton.startAnimation(slideAnimationDown)
        addFloatingButton.startAnimation(rotateAnimationClose)
        clicked = !clicked
    }


    private fun hideAddFolderToSelectTitleCardView() = with(binding) {
        addFloatingButton.setImageResource(R.drawable.ic_baseline_add_24)
        addFloatingButton.alpha = 1f
        addFloatingButton.isEnabled = true
        addFolderToSelectTitleCardView.isGone = true
        addFolderBackgroundTouchView.isGone = true
    }

}