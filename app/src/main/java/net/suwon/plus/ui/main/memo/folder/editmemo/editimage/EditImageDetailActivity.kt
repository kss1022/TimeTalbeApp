package net.suwon.plus.ui.main.memo.folder.editmemo.editimage


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import net.suwon.plus.R
import net.suwon.plus.databinding.ActivityEditImageDetailBinding
import net.suwon.plus.ui.base.BaseActivity
import net.suwon.plus.ui.main.memo.folder.editmemo.EditMemoFragment
import net.suwon.plus.ui.main.memo.folder.editmemo.EditMemoFragment.Companion.EXTRA_IMAGE_LIST
import net.suwon.plus.ui.main.memo.folder.editmemo.EditMemoFragment.Companion.EXTRA_IMAGE_POSITION
import net.suwon.plus.widget.adapter.mediaadpater.MediaImageClickListener
import net.suwon.plus.widget.adapter.mediaadpater.MemoImageAdapter
import javax.inject.Inject


class EditImageDetailActivity :
    BaseActivity<EditImageDetailViewModel, ActivityEditImageDetailBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: EditImageDetailViewModel by viewModels { viewModelFactory }

    override fun getViewBinding(): ActivityEditImageDetailBinding =
        ActivityEditImageDetailBinding.inflate(layoutInflater)


    private val detailAdapter: MemoImageAdapter by lazy {
        MemoImageAdapter(object : MediaImageClickListener {
            override fun itemClick(position: Int) {
            }
        })
    }
    private val pagerSnapHelper = PagerSnapHelper()


    lateinit var imageList : ArrayList<String>

    override fun initViews() {
        val position = intent.getIntExtra(EXTRA_IMAGE_POSITION, 0)
         imageList = intent.getStringArrayListExtra(EXTRA_IMAGE_LIST) as ArrayList<String>

        viewModel.bindingItemAdapterPosition.set(position)
        viewModel.itemCount = imageList?.size ?: 0

        detailAdapter.setUrlList(imageList?.toMutableList() ?: listOf())

        scrollToPosition()
        initRecyclerView()

        bindViews()
        setCountTextView()
    }


    override fun observeData() {
        viewModel.checkBoxClickEvent.observe(this) { item ->
            item?.let { pair->
                pair.second?.let { it->
                    viewModel.selection.toggle(pair.first, it)
                    viewModel.isChecked.value = viewModel.selection.isSelected(item.first)
                    setCheckedImage()
                }
            }
        }
    }


    private fun initRecyclerView() = with(binding) {
        recyclerView.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addOnScrollListener(onScrollListener)
        }
        pagerSnapHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun bindViews() {
        binding.checked.setOnClickListener {
            viewModel.onCheckBoxClick()
            setCountTextView()
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.deleteButton.setOnClickListener {
            this.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(
                    EditMemoFragment.DELETE_IMAGE,
                    ArrayList(viewModel.getSelectedMediaList())
                )
            })
            finish()
        }
    }


    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        fun onScrolled() {
            val currentPosition =
                (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (currentPosition != RecyclerView.NO_POSITION) {
                viewModel.currentMediaItem = currentPosition to imageList[currentPosition]

                    viewModel.currentMediaItem?.let { item ->
                    viewModel.isChecked.value =
                        viewModel.selection.isSelected(item.first)
                    setCheckedImage()
                    binding.toolBarTextView.text =
                        "${currentPosition + 1} / ${viewModel.itemCount}"
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            viewModel.checkBoxEnabled.value = (newState == RecyclerView.SCROLL_STATE_IDLE)
            onScrolled()
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScrolled()
        }
    }

    private fun scrollToPosition() {
        binding.recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = binding.recyclerView.layoutManager
                layoutManager?.let {
                    val currentPosition = viewModel.bindingItemAdapterPosition.get()
                    val viewAtPosition = layoutManager.findViewByPosition(currentPosition)
                    if (viewAtPosition == null || layoutManager.isViewPartiallyVisible(
                            viewAtPosition,
                            false,
                            true
                        )
                    ) {
                        binding.recyclerView.post { layoutManager.scrollToPosition(currentPosition) }
                    }
                }

            }
        })
    }


    private fun setCheckedImage() {
        viewModel.isChecked.value?.let { checked ->
            if (checked) {
                binding.checked.setImageResource(R.drawable.check_circle_on_24)
            } else {
                binding.checked.setImageResource(R.drawable.check_circle_off_24)
            }
        }
    }

    private fun setCountTextView() {
        viewModel.selection.getCount().value?.let { count ->
            if (count > 0) {
                binding.deleteButton.setTextColor(ContextCompat.getColor(this, R.color.blue_light))
                binding.countTextView.text = count.toString()
            } else {
                binding.deleteButton.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorOnPrimary
                    )
                )
                binding.countTextView.text = ""
            }
        }
    }


    companion object {
        fun newIntent(context: Context) = Intent(context, EditImageDetailActivity::class.java)
    }

}