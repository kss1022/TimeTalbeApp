package net.suwon.plus.ui.main.memo.folder.editmemo.gallery.detail


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collectLatest
import net.suwon.plus.R
import net.suwon.plus.databinding.FragmentMediaDetailBinding
import net.suwon.plus.ui.main.memo.folder.editmemo.EditMemoFragment
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.GallerySharedViewModel
import net.suwon.plus.widget.adapter.mediaadpater.DetailAdapter
import javax.inject.Inject


class MediaDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelMedia: MediaDetailViewModel by viewModels { viewModelFactory }

    private val sharedViewModel: GallerySharedViewModel by activityViewModels{viewModelFactory}

    private var _bindnig: FragmentMediaDetailBinding? = null
    private val binding get() = _bindnig!!

    private val detailAdapter: DetailAdapter by lazy {
        DetailAdapter()
    }
    private val pagerSnapHelper = PagerSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            sharedViewModel.items.collectLatest { detailAdapter.submitData(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindnig = FragmentMediaDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }


    private fun initView() {
        scrollToPosition()

        initRecyclerView()
        detailAdapter.selection = sharedViewModel.selection

        bindViews()
        setCountTextView()
    }

    private fun observeData() {
        viewModelMedia.checkBoxClickEvent.observe(viewLifecycleOwner) { item ->
            item?.let {
                sharedViewModel.selection.toggle(item.getId(), item.media)
                viewModelMedia.isChecked.value = sharedViewModel.selection.isSelected(item.getId())
                setCheckedImage()
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
            viewModelMedia.onCheckBoxClick()
            setCountTextView()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.completeButton.setOnClickListener {
            requireActivity().setResult(Activity.RESULT_OK, Intent().apply {
                putParcelableArrayListExtra(
                    EditMemoFragment.GET_IMAGE,
                    ArrayList(sharedViewModel.getSelectedMediaList()))
            })
            requireActivity().finish()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        val currentPosition =
            (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        sharedViewModel.bindingItemAdapterPosition.set(currentPosition)

        _bindnig = null
    }


    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        fun onScrolled() {
            val currentPosition =
                (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (currentPosition != RecyclerView.NO_POSITION) {
                viewModelMedia.currentMediaItem = detailAdapter.peek(currentPosition)
                viewModelMedia.currentMediaItem?.let { item ->
                    viewModelMedia.isChecked.value =
                        sharedViewModel.selection.isSelected(item.getId())
                    setCheckedImage()
                    binding.toolBarTextView.text =
                        "${currentPosition + 1} / ${sharedViewModel.itemCount.value}"
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            viewModelMedia.checkBoxEnabled.value = (newState == RecyclerView.SCROLL_STATE_IDLE)
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
                    val currentPosition = sharedViewModel.bindingItemAdapterPosition.get()
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
        viewModelMedia.isChecked.value?.let { checked ->
            if (checked) {
                binding.checked.setImageResource(R.drawable.check_circle_on_24)
            } else {
                binding.checked.setImageResource(R.drawable.check_circle_off_24)
            }
        }
    }

    private fun setCountTextView(){
        sharedViewModel.selection.getCount().value?.let { count->
            if(count > 0){
                binding.completeButton.setTextColor( ContextCompat.getColor(requireContext(), R.color.blue_light))
                binding.countTextView.text = count.toString()
            }else{
                binding.completeButton.setTextColor( ContextCompat.getColor(requireContext(), R.color.colorOnPrimary))
                binding.countTextView.text = ""
            }
        }
    }
}