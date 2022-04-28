package net.suwon.plus.ui.main.time_table.addTimeTable.addcell

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelBuilder
import net.suwon.plus.di.ViewModelKey
import net.suwon.plus.di.scope.FragmentScope
import net.suwon.plus.ui.main.time_table.addTimeTable.addcell.lecturelist.LectureListFragment
import net.suwon.plus.ui.main.time_table.addTimeTable.addcell.lecturelist.LectureListFragmentModule


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