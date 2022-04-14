package com.example.suwon_university_community.ui.main.memo.folder.memolist

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.FragmentMemoListBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

class MemoListFragment : BaseFragment<MemoListViewModel, FragmentMemoListBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: MemoListViewModel by viewModels { viewModelFactory }

    override fun getViewBinding(): FragmentMemoListBinding = FragmentMemoListBinding.inflate(layoutInflater)

    private val arguments : MemoListFragmentArgs by navArgs()


    private lateinit var callback: OnBackPressedCallback


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }



    override fun observeData() {}

    override fun initViews() {
        arguments.folderId
        bindViews()
    }


    private fun bindViews() = with(binding){
        addMemoFloatingButton.setOnClickListener {
            findNavController().navigate(R.id.editMemoFragment)
        }
    }
}