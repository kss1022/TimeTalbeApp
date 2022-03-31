package com.example.suwon_university_community.ui.main.time_table.addTimeTable.tablelist

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TimeTableListActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(TimeTableListViewModel::class)
    abstract fun bindViewModel( vidModel : TimeTableListViewModel) : ViewModel

}