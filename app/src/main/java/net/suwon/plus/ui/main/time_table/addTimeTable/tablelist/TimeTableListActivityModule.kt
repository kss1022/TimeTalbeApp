package net.suwon.plus.ui.main.time_table.addTimeTable.tablelist

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelKey

@Module
abstract class TimeTableListActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(TimeTableListViewModel::class)
    abstract fun bindViewModel( vidModel : TimeTableListViewModel) : ViewModel

}