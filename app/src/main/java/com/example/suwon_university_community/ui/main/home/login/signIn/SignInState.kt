package com.example.suwon_university_community.ui.main.home.login.signIn

import androidx.annotation.StringRes

sealed class SignInState {

    object Uninitialized : SignInState()

    object Loading : SignInState()

    object Success : SignInState()

    data class Error(
        @StringRes val massageId: Int
    ) : SignInState()
}