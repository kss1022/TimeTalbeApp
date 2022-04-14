package com.example.suwon_university_community.ui.main.home.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.ActivityAuthBinding
import dagger.android.support.DaggerAppCompatActivity

class AuthActivity : DaggerAppCompatActivity() {


    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
    }


   private fun initView() {


       val navigationController =
            (supportFragmentManager.findFragmentById(R.id.navigationContainerView) as NavHostFragment).navController
        binding.toolbar.setupWithNavController(navigationController)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }
}