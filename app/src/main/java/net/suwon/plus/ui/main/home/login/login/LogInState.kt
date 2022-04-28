package net.suwon.plus.ui.main.home.login.login

import androidx.annotation.StringRes

sealed class LogInState {

    object Uninitialized : LogInState()

    object Loading : LogInState()

    data class Success(
        val verified: Boolean
    ) : LogInState()


    data class Error(
        @StringRes val massageId: Int
    ) : LogInState()
}