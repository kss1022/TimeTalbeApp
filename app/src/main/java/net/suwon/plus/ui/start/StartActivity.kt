package net.suwon.plus.ui.start

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import net.suwon.plus.R
import net.suwon.plus.data.preference.PreferenceManager
import net.suwon.plus.databinding.ActivityStartBinding
import net.suwon.plus.ui.base.BaseActivity
import net.suwon.plus.ui.main.MainActivity
import javax.inject.Inject

class StartActivity : BaseActivity<StartActivityViewModel, ActivityStartBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: StartActivityViewModel by viewModels<StartActivityViewModel> {
        viewModelFactory
    }

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeType = preferenceManager.getThemeType()
        themeType?.let {
            AppCompatDelegate.setDefaultNightMode(themeType)
        }
    }

    override fun getViewBinding(): ActivityStartBinding =
        ActivityStartBinding.inflate(layoutInflater)


    override fun observeData() = viewModel.startStateLiveData.observe(this) {
        when (it) {
            is StartState.Loading -> {
                handleLoadingState()
            }
            is StartState.Success -> {
                handleSuccessState()
            }

            else -> Unit
        }
    }


    private fun handleLoadingState() {
        binding.loadingTextView.text = getString(R.string.loading)
    }


    private fun handleSuccessState() {
        startActivity(MainActivity.newIntent(this))
        finish()
    }

}
