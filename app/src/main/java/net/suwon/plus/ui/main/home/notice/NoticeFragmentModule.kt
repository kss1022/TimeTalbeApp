package net.suwon.plus.ui.main.home.notice

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class NoticeFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoticeViewModel::class)
    abstract fun bindViewModel(viewModel: NoticeViewModel): ViewModel
}