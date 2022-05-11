package net.suwon.plus.ui.main.memo.folder.editmemo.gallery.album


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job
import net.suwon.plus.data.entity.media.AlbumItem
import net.suwon.plus.databinding.FragmentAlbumBinding
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.GallerySharedViewModel
import net.suwon.plus.util.GridSpaceDecoration
import net.suwon.plus.util.PagingConstants
import net.suwon.plus.widget.adapter.mediaadpater.AlbumAdapter
import net.suwon.plus.widget.adapter.mediaadpater.AlbumClickListener
import javax.inject.Inject


class AlbumFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val viewModel: AlbumViewModel by viewModels { viewModelFactory }
    private lateinit var fetchJob: Job


    private val sharedViewModel: GallerySharedViewModel by  activityViewModels{viewModelFactory}


    private var _bindnig: FragmentAlbumBinding? = null
    private val binding get() = _bindnig!!

    private val albumAdapter: AlbumAdapter by lazy {
        AlbumAdapter(requireContext(), object : AlbumClickListener {
            override fun itemClick(item: AlbumItem) {
                sharedViewModel.setBucketId(item.album)
                findNavController().navigateUp()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindnig = FragmentAlbumBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }


    private fun initView() {
        fetchJob = viewModel.fetchData()

        initRecyclerView()

        bindViews()
    }

    private fun observeData() {
        viewModel.items.observe(viewLifecycleOwner) {
            albumAdapter.submitList(it)
        }

    }

    private fun initRecyclerView() = with(binding) {
        recyclerView.apply {
            this.adapter = albumAdapter
            layoutManager = GridLayoutManager(context, PagingConstants.DEFAULT_SPAN_COUNT)
            addItemDecoration(GridSpaceDecoration())
            setHasFixedSize(true)
        }
    }

    private fun bindViews() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolBarTextView.setOnClickListener {
            findNavController().navigateUp()
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


}