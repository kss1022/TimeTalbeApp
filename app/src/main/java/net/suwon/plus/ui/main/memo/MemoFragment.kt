package net.suwon.plus.ui.main.memo

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import net.suwon.plus.R
import net.suwon.plus.databinding.FragmentMemoBinding
import net.suwon.plus.ui.base.BaseFragment
import javax.inject.Inject

class MemoFragment : BaseFragment<MemoViewModel, FragmentMemoBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: MemoViewModel by viewModels<MemoViewModel> { viewModelFactory }


    override fun getViewBinding(): FragmentMemoBinding = FragmentMemoBinding.inflate(layoutInflater)


    override fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
        val navigationController =
            ( childFragmentManager.findFragmentById(R.id.navigationContainerView) as NavHostFragment).navController

        binding.toolBar.setupWithNavController( navigationController)
    }


    companion object {
        fun newInstance() = MemoFragment()

        const val TAG = "MemoFragment"
    }



}