package com.example.suwon_university_community.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.suwon_university_community.R
import com.example.suwon_university_community.ui.login.AuthActivity
import com.example.suwon_university_community.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth


    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                startActivity(MainActivity.newIntent(this))
            }
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        if (auth.currentUser == null) {
            loginLauncher.launch(AuthActivity.newIntent(this))
        } else {
            startActivity(MainActivity.newIntent(this))
            finish()
        }
    }
}
