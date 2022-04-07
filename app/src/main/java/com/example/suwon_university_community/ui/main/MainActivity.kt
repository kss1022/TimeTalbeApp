package com.example.suwon_university_community.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.ActivityMainBinding
import com.example.suwon_university_community.ui.main.chat.ChatFragment
import com.example.suwon_university_community.ui.main.home.HomeFragment
import com.example.suwon_university_community.ui.main.time_table.TimeTableFragment
import com.google.android.material.navigation.NavigationBarView
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding


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
            R.id.menu_chat -> {
                showFragment(ChatFragment.newInstance() , ChatFragment.TAG)
                 true
            }
            R.id.menu_time_table -> {
                showFragment(TimeTableFragment.newInstance() , TimeTableFragment.TAG)
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