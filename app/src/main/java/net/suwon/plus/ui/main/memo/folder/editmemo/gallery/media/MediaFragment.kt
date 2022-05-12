package net.suwon.plus.ui.main.memo.folder.editmemo.gallery.media

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.suwon.plus.R
import net.suwon.plus.data.entity.media.MediaItem
import net.suwon.plus.databinding.FragmentMediaBinding
import net.suwon.plus.ui.main.memo.folder.editmemo.EditMemoFragment
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.GallerySharedViewModel
import net.suwon.plus.util.DeviceUtil
import net.suwon.plus.util.GridSpaceDecoration
import net.suwon.plus.util.PagingConstants.DEFAULT_SPAN_COUNT
import net.suwon.plus.util.lifecycle.SingleLiveEvent
import net.suwon.plus.widget.adapter.mediaadpater.MediaAdapter
import net.suwon.plus.widget.adapter.mediaadpater.MediaClickListener
import javax.inject.Inject


class MediaFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MediaViewModel by viewModels { viewModelFactory }

    private val sharedViewModel: GallerySharedViewModel by activityViewModels{viewModelFactory}


    private lateinit var fetchJob: Job


    private val navigateAlbumEvent = SingleLiveEvent<Unit>()


    private var _bindnig: FragmentMediaBinding? = null
    private val binding get() = _bindnig!!

    private val mediaAdapter: MediaAdapter by lazy {
        MediaAdapter(object : MediaClickListener {
            override fun itemClick(view: View, item: MediaItem, position: Int) {
                sharedViewModel.onItemClick(view, item, position)
            }

            override fun checkBoxClick(item: MediaItem) {
                sharedViewModel.onCheckBoxClick(item)
                setCountTextView()
            }
        })
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindnig = FragmentMediaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (DeviceUtil.hasPermission(requireContext())) {
            initView()
        } else {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    initView()
                    sharedViewModel.repository.invalidate()
                } else {
                    findNavController().popBackStack()
                }
            }.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


        observeData()
        setCountTextView()
    }


    private fun observeData() {
        lifecycleScope.launch {
            sharedViewModel.items.collectLatest {
                mediaAdapter.submitData(it)
            }
        }

        sharedViewModel.currentFolder.observe(viewLifecycleOwner) { album ->
            binding.toolBarTextView.text = ((album?.name ?: "최신항목") + " ▾")
        }

        sharedViewModel.itemClickEvent.observe(viewLifecycleOwner) { triple ->

            triple?.let {
                val view = it.first
                val media = it.second
                val position = it.third

                sharedViewModel.bindingItemAdapterPosition.set(position)

                val navDirection =
                    MediaFragmentDirections.actionMediaFragmentToMediaDetailFragment(
                        media,
                        position
                    )


                findNavController().navigate(navDirection)
            }
        }

        navigateAlbumEvent.observe(viewLifecycleOwner){
            findNavController().navigate(R.id.action_mediaFragment_to_albumFragment)
        }
    }


    private fun initView() {
        scrollToPosition()

        fetchJob = viewModel.fetchData()


        initRecyclerView()
        mediaAdapter.selection = sharedViewModel.selection


        bindViews()
    }


    private fun initRecyclerView() = with(binding) {
        recyclerView.apply {
            this.adapter = mediaAdapter
            layoutManager = GridLayoutManager(context, DEFAULT_SPAN_COUNT)
            addItemDecoration(GridSpaceDecoration())
            setHasFixedSize(true)
        }
    }

    private fun bindViews() {
        binding.toolBarTextView.setOnClickListener {
            navigateAlbumEvent.call()
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }

        binding.completeButton.setOnClickListener {
            requireActivity().setResult(Activity.RESULT_OK, Intent().apply {
                putParcelableArrayListExtra(EditMemoFragment.GET_IMAGE,
                    ArrayList(sharedViewModel.getSelectedMediaList()))
            })
            requireActivity().finish()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bindnig = null
    }

    override fun onDestroy() {
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }

        super.onDestroy()
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
                    val currentPosition =
                        sharedViewModel.bindingItemAdapterPosition.get()
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