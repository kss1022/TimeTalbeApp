package net.suwon.plus.ui.main.home.setting

import androidx.annotation.StringRes

sealed class SettingState {
    object Uninitialized : SettingState()

    object NotLogin : SettingState()

    data class Login(
        val isVerify: Boolean
    ) : SettingState()

    data class Message(
        @StringRes val massageId: Int
    ) :SettingState()
}