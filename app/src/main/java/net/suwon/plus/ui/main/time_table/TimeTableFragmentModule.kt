package net.suwon.plus.ui.main.time_table

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class TimeTableFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(TimeTableViewModel::class)
    abstract fun bindViewModel(viewModel: TimeTableViewModel): ViewModel


}