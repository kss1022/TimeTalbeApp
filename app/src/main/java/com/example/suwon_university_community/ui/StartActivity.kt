package com.example.suwon_university_community.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.suwon_university_community.R
import com.example.suwon_university_community.ui.login.AuthActivity
import com.example.suwon_university_community.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        if(auth.currentUser == null){
            startActivity(AuthActivity.newIntent(this))
        }else{
            startActivity(MainActivity.newIntent(this))
        }
    }

    private var auth : FirebaseAuth = Firebase.auth
}