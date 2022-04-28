package net.suwon.plus.ui.main.memo.folder

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import net.suwon.plus.R
import net.suwon.plus.data.entity.memo.FolderCategory
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.databinding.FolderSelectBottomSheetBinding
import net.suwon.plus.model.FolderModel
import net.suwon.plus.widget.adapter.CustomAdapter.MemoListBottomSheetAdapter
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FolderSelectSheetFragment : BottomSheetDialogFragment(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    @Inject
    lateinit var memoRepository: MemoRepository

    private var _binding: FolderSelectBottomSheetBinding? = null
    private val binding get() = _binding!!



    private  lateinit var modelAdapter : MemoListBottomSheetAdapter


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.Widget_Suwon_University_Community_BottomSheetDialog
        )
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FolderSelectBottomSheetBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::memoRepository.isInitialized) {
            initView()
            bindView()
        } else {
            dialog?.dismiss()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    private fun initView() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(coroutineContext) {
                memoRepository.getFolderList().collect { list->

                    val folderList =  list.filter { it.category == FolderCategory.MEMO || it.category == FolderCategory.TIME_TABLE }.map {
                        FolderModel(
                            id = it.folderId,
                            name = it.name,
                            count = it.count,
                            category = it.category,
                            isDefault = it.isDefault,
                            timeTableId = it.timeTableId,
                        )
                    }

                    val size = folderList.size
                    var currentPosition  = 0
                    val currentFolder  = arguments?.getLong(MEMO_FOLDER_LIST)

                    for ( i in 0 until  size){
                        if(folderList[i].id == currentFolder) currentPosition = i
                    }

                    modelAdapter = MemoListBottomSheetAdapter(folderList)
                    Log.e("folderList" , folderList.toString())
                    modelAdapter.selectedPosition = currentPosition

                    initRecyclerView()
                }
            }
        }
    }

    private fun initRecyclerView() = with(binding){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = modelAdapter
        }
    }


    private fun bindView() = with(binding){
        positiveButton.setOnClickListener {
            if(::modelAdapter.isInitialized){
                itemClickListener( modelAdapter.getSelectedFolderId() )
            }

            dialog?.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog?.dismiss()
        }
    }


    companion object {
        fun newInstance(
            folderId : Long,
            itemClickListener: (Long) -> Unit
        ): FolderSelectSheetFragment {

            Companion.itemClickListener = itemClickListener
            return FolderSelectSheetFragment().apply {
                arguments = bundleOf(
                    MEMO_FOLDER_LIST to folderId
                )
            }
        }


        lateinit var itemClickListener: (Long) -> Unit

        const val TAG = "TimeTableBottomSheet"
        const val MEMO_FOLDER_LIST = "MemoFolderList"
    }


}