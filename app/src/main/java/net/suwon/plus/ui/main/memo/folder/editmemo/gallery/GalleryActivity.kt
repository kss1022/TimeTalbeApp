package net.suwon.plus.ui.main.memo.folder.editmemo.gallery

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import net.suwon.plus.databinding.ActivityGalleryBinding
import net.suwon.plus.ui.base.BaseActivity
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject


class GalleryActivity : BaseActivity<BaseViewModel, ActivityGalleryBinding>() {


    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory


    override val viewModel: BaseViewModel by viewModels { viewModelFactory }
    private val sharedViewModel: GallerySharedViewModel by viewModels { viewModelFactory }


    private val contentObserver by lazy {
        ContentObserver(this)
    }


    override fun getViewBinding(): ActivityGalleryBinding =
        ActivityGalleryBinding.inflate(layoutInflater)


    override fun observeData() {
        lifecycle.addObserver(sharedViewModel)
        lifecycle.addObserver(contentObserver)

        contentObserver.getContentChangedEvent().observe(this) {
            sharedViewModel.repository.invalidate()
        }
    }

    override fun initViews() {
    }


    companion object{
        fun newIntent(context : Context) = Intent(context, GalleryActivity::class.java)
    }
}