package net.suwon.plus.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import dagger.android.support.DaggerAppCompatActivity
import net.suwon.plus.R
import net.suwon.plus.databinding.ActivityMainBinding
import net.suwon.plus.ui.main.home.HomeFragment
import net.suwon.plus.ui.main.memo.MemoFragment
import net.suwon.plus.ui.main.time_table.TimeTableFragment
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sharedViewModel: MainActivitySharedViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance() , HomeFragment.TAG)
                 true
            }

            R.id.menu_time_table -> {
                showFragment(TimeTableFragment.newInstance() , TimeTableFragment.TAG)
                 true
            }

            R.id.menu_memo -> {
                showFragment(MemoFragment.newInstance() , MemoFragment.TAG)
                true
            }


            else ->  false
        }
    }



    private fun initViews() = with(binding) {
        bottomNavigationView.setOnItemSelectedListener(this@MainActivity)

        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }

    fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)

        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().hide(it).commitAllowingStateLoss()
        }

        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }

    }


    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java).apply {  }
    }
}