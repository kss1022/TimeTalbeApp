package net.suwon.plus.ui.main.home

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class HomeFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel
}