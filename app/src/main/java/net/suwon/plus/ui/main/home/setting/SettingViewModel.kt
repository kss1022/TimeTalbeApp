package net.suwon.plus.ui.main.home.setting

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.R
import net.suwon.plus.data.preference.PreferenceManager
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {


    val settingStateLiveData = MutableLiveData<SettingState>(SettingState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {

        preferenceManager.getIdToken()?.let {
            var isVerified = preferenceManager.getVerified()

            if (isVerified.not()) {
                isVerified = auth.currentUser!!.isEmailVerified

                Log.e("isEmailVerified", auth.currentUser!!.isEmailVerified.toString())
                preferenceManager.putVerified(isVerified)
            }

            Log.e("isEmailVerified", auth.currentUser!!.isEmailVerified.toString())


            settingStateLiveData.value = SettingState.Login(isVerified)
        } ?: kotlin.run {
            settingStateLiveData.value = SettingState.NotLogin
        }
    }


    fun logout() {

        auth.signOut()
        preferenceManager.removeIdToken()
        preferenceManager.removeVerified()
        fetchData()
    }


    fun changePassWord(currentPassword: String, newPassword: String) {
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val credential = EmailAuthProvider
                .getCredential(user.email!!, currentPassword)

            user.reauthenticate(credential).addOnCompleteListener {
                user.updatePassword(newPassword)?.addOnSuccessListener {
                    settingStateLiveData.value =
                        SettingState.Message(R.string.change_password_success)
                }?.addOnFailureListener {
                    settingStateLiveData.value = SettingState.Message(R.string.change_password_fail)
                }
            }.addOnFailureListener{
                settingStateLiveData.value = SettingState.Message(R.string.current_password_fail)
            }
        }


    }

    fun signOut(password: String) {
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val credential = EmailAuthProvider
                .getCredential(user.email!!, password)

            user.reauthenticate(credential).addOnCompleteListener {
               user.delete().addOnSuccessListener {
                   preferenceManager.removeIdToken()
                   preferenceManager.removeVerified()
                   settingStateLiveData.value = SettingState.Message(R.string.signOut_success)
                   fetchData()
               }.addOnFailureListener {
                   settingStateLiveData.value = SettingState.Message(R.string.signOut_fail)
               }

            }.addOnFailureListener{
                settingStateLiveData.value = SettingState.Message(R.string.current_password_fail)
            }
        }
    }

    fun sendVerifyEmail() {
        auth.setLanguageCode("ko")
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { verificationTask ->
                if (verificationTask.isSuccessful) {
                    settingStateLiveData.value =
                        SettingState.Message(R.string.send_email_check_please)
                } else {
                    settingStateLiveData.value =
                        SettingState.Message(R.string.email_send_failed_click_button_please)
                }
            }
    }

}