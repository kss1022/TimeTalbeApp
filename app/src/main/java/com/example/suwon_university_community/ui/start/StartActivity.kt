package com.example.suwon_university_community.ui.start

import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.databinding.ActivityStartBinding
import com.example.suwon_university_community.ui.base.BaseActivity
import com.example.suwon_university_community.ui.main.MainActivity
import javax.inject.Inject

class StartActivity : BaseActivity<StartActivityViewModel, ActivityStartBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: StartActivityViewModel by viewModels<StartActivityViewModel> {
        viewModelFactory
    }


    override fun getViewBinding(): ActivityStartBinding = ActivityStartBinding.inflate(layoutInflater)


    override fun observeData() = viewModel.startStateLiveData.observe(this){
        when(it){
            is StartState.Loading -> {
                handleLoadingState()
            }
            is StartState.Success -> {
                handleSuccessState()
            }

            else->Unit
        }
    }


    private fun handleLoadingState() {
        binding.loadingTextView.text = "로딩 하는 중이요~"
    }


    private fun handleSuccessState() {
            startActivity(MainActivity.newIntent(this))
            finish()
    }


//    private val loginLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                startActivity(MainActivity.newIntent(this))
//            }
//            finish()
//        }

}
