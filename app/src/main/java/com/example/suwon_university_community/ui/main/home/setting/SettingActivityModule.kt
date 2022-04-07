package com.example.suwon_university_community.ui.main.home.setting

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SettingActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    abstract fun bindViewModel( vidModel : SettingViewModel) : ViewModel
}