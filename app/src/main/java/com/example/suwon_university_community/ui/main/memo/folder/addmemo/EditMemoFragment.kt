package com.example.suwon_university_community.ui.main.memo.folder.addmemo

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.suwon_university_community.databinding.FragmentEditMemoBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

class EditMemoFragment :  BaseFragment<EditMemoViewModel , FragmentEditMemoBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: EditMemoViewModel by viewModels { viewModelFactory }

    override fun getViewBinding(): FragmentEditMemoBinding  = FragmentEditMemoBinding.inflate(layoutInflater)


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


    override fun observeData() {
    }


    override fun initViews() {
    }

}