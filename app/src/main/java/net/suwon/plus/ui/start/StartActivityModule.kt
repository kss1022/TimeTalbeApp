package net.suwon.plus.ui.start

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class StartActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(StartActivityViewModel::class)
    abstract fun bindViewModel(viewModel: StartActivityViewModel): ViewModel
}