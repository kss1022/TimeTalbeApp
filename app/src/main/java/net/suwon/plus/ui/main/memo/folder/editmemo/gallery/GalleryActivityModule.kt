package net.suwon.plus.ui.main.memo.folder.editmemo.gallery

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.suwon.plus.di.ViewModelBuilder
import net.suwon.plus.di.ViewModelKey
import net.suwon.plus.di.scope.FragmentScope
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.album.AlbumFragment
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.album.AlbumFragmentModule
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.detail.MediaDetailFragment
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.detail.MediaDetailFragmentModule
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.media.MediaFragment
import net.suwon.plus.ui.main.memo.folder.editmemo.gallery.media.MediaFragmentModule

@Module
abstract class GalleryActivityModule {

    companion object {
        @Provides
        fun provideApplication(activity: GalleryActivity): Application {
            return activity.application
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(GallerySharedViewModel::class)
    abstract fun bindSharedViewModel(viewModel: GallerySharedViewModel): ViewModel


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            AlbumFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getAlbumFragment(): AlbumFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            MediaDetailFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getMediaDetailFragment(): MediaDetailFragment


    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            MediaFragmentModule::class,
            ViewModelBuilder::class
        ]
    )
    protected abstract fun getMediaFragment(): MediaFragment
}