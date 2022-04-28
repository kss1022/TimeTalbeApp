package net.suwon.plus.ui.start

sealed class StartState {

    object Uninitialized : StartState()
    object Loading : StartState()
    object Success : StartState()
}