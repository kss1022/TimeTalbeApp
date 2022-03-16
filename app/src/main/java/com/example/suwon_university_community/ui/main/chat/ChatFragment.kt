package com.example.suwon_university_community.ui.main.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.databinding.FragmentChatBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: ChatViewModel by viewModels<ChatViewModel> { viewModelFactory }


    override fun getViewBinding(): FragmentChatBinding = FragmentChatBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun initViews() {
        super.initViews()
    }

    override fun observeData() {
    }

    companion object{
        fun newInstance() = ChatFragment()

        const val TAG = "ChatFragment"
    }
}