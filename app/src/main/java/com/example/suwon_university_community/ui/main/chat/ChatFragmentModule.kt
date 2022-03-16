package com.example.suwon_university_community.ui.main.chat

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindViewModel(viewModel: ChatViewModel): ViewModel
}