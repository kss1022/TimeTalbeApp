package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.di.ViewModelBuilder
import com.example.suwon_university_community.di.ViewModelKey
import com.example.suwon_university_community.di.scope.FragmentScope
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.lecturelist.LectureListFragment
import com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.lecturelist.LectureListFragmentModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
abstract class AddTimeTableCellActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddTimeTableCellViewModel::class)
    abstract fun bindViewModel(viewModel: AddTimeTableCellViewModel): ViewModel


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            LectureListFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getLectureListFragment(): LectureListFragment
}