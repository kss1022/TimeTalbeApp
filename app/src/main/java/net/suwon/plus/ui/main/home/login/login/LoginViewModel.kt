package net.suwon.plus.ui.main.home.login.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import net.suwon.plus.R
import net.suwon.plus.data.preference.PreferenceManager
import net.suwon.plus.ui.base.BaseViewModel
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