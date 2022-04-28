package net.suwon.plus.ui.main.home.setting

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class SettingActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    abstract fun bindViewModel( vidModel : SettingViewModel) : ViewModel
}