package net.suwon.plus.ui.main.memo.folder.editmemo.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import net.suwon.plus.databinding.ActivityGalleryBinding
import javax.inject.Inject


class GalleryActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sharedViewModel: GallerySharedViewModel by viewModels { viewModelFactory }


    private val contentObserver by lazy {
        ContentObserver(this)
    }

    private lateinit var  binding : ActivityGalleryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()
    }


    private fun observeData() {
        lifecycle.addObserver(sharedViewModel)
        lifecycle.addObserver(contentObserver)

        contentObserver.getContentChangedEvent().observe(this) {
            sharedViewModel.repository.invalidate()
        }
    }


    companion object{
        fun newIntent(context : Context) = Intent(context, GalleryActivity::class.java)
    }
}