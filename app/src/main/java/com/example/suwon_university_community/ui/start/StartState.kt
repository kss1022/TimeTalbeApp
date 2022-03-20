package com.example.suwon_university_community.ui.start

sealed class StartState {

    object Uninitialized : StartState()
    object Loading : StartState()
    object Success : StartState()
}