package com.example.suwon_university_community.ui.login.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.repository.user.UserRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    private val userRepository : UserRepository
) : BaseViewModel() {



    val signInStateLiveData =  MutableLiveData<SignInState>(SignInState.Uninitialized)


    fun sendEmail(email: String, password: String) = viewModelScope.launch {
        signInStateLiveData.value = SignInState.Loading

        //회원가입에는 성공했으나 인증메일에 실패한 경우
        auth.currentUser?.let {
            it.sendEmailVerification()
            signInStateLiveData.value = SignInState.Success
            return@launch
        }


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    viewModelScope.launch {
                        userRepository.saveUserId(auth.currentUser?.uid!!)
                    }


                    auth.setLanguageCode("ko")
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                signInStateLiveData.value = SignInState.Success
                            } else {
                                signInStateLiveData.value = SignInState.Error(R.string.email_send_failed_click_button_please)
                            }
                        }
                } else{
                    signInStateLiveData.value = SignInState.Error(R.string.signIn_fail_check_your_email)
                }
            }
    }

    fun signOut() {
        if(auth.currentUser != null) auth.signOut()
    }


}