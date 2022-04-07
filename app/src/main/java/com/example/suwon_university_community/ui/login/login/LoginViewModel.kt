package com.example.suwon_university_community.ui.login.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {


    val loginStateLiveData = MutableLiveData<LogInState>(LogInState.Uninitialized)


    fun login(email: String, password: String) = viewModelScope.launch {

        loginStateLiveData.value = LogInState.Loading


        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveIdToken()

                    val isEmailVerified = auth.currentUser!!.isEmailVerified

                    loginStateLiveData.value = LogInState.Success(isEmailVerified)
                    preferenceManager.putVerified(isEmailVerified)
                    preferenceManager.putRecentlyLoginId(email)
                } else {
                    loginStateLiveData.value = LogInState.Error(R.string.login_fail)
                }
            }
    }


    private fun saveIdToken() {
        auth.currentUser?.let { user ->
            user.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    preferenceManager.putIdToken(task.result.token!!)
                }
            }
        }
    }

    fun sendPasswordResetEmail( emailAdress : String){
        loginStateLiveData.value = LogInState.Loading

        auth.setLanguageCode("ko")
        auth.sendPasswordResetEmail(emailAdress).addOnSuccessListener {
            loginStateLiveData.value = LogInState.Error(R.string.password_reset_mail_send)
        }.addOnFailureListener {
            loginStateLiveData.value = LogInState.Error(R.string.password_reset_mail_fail)

        }
    }
}