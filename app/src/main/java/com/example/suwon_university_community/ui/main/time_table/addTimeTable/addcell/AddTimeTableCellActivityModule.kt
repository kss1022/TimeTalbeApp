package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import com.example.suwon_university_community.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
abstract class AddTimeTableCellActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddTimeTableCellViewModel::class)
    abstract fun bindViewModel(viewModel: AddTimeTableCellViewModel): ViewModel


}