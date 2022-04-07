package com.example.suwon_university_community.ui.main.home.setting

import android.app.Activity
import android.app.AlertDialog
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.databinding.ActivitySettingBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.ui.base.BaseActivity
import com.example.suwon_university_community.ui.login.AuthActivity
import javax.inject.Inject

class SettingActivity : BaseActivity<SettingViewModel, ActivitySettingBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: SettingViewModel by viewModels { viewModelFactory }

    override fun getViewBinding(): ActivitySettingBinding =
        ActivitySettingBinding.inflate(layoutInflater)


    @Inject
    lateinit var preferenceManager: PreferenceManager

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                finish()
            }
        }

    override fun observeData() = viewModel.settingStateLiveData.observe(this) {
        when (it) {
            is SettingState.NotLogin -> {
                handleNotLoginState()
            }

            is SettingState.Login -> {
                handleLoginState(it)
            }

            is SettingState.Message -> {
                Toast.makeText(this, getString(it.massageId), Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }


    private fun handleNotLoginState() = with(binding) {
        logoutTextView.isGone = true
        changePasswordTextView.isGone = true
        signOutTextView.isGone = true

        loginTextView.apply {
            text = getString(R.string.need_login)
            setOnClickListener {
                loginLauncher.launch(AuthActivity.newIntent(this@SettingActivity))
            }
        }
    }


    private fun handleLoginState(settingState: SettingState.Login) = with(binding) {
        logoutTextView.visibility = View.VISIBLE
        changePasswordTextView.visibility = View.VISIBLE
        signOutTextView.visibility = View.VISIBLE

        if (settingState.isVerify) {
            loginTextView.apply {
                text = getString(R.string.email_is_verified)
                setOnClickListener {
                }
            }
        } else {
            loginTextView.apply {
                text = getString(R.string.email_is_not_verified)
                setOnClickListener {
                    showSendEmailAlertDialog()
                }
            }

        }

    }


    override fun initViews() = with(binding) {

        toolBar.setNavigationOnClickListener {
            finish()
        }

        preferenceManager.getThemeType()?.let {
            setThemeTextView(it)
        }

        bindViews()
    }


    private fun bindViews() = with(binding) {


        themeTextView.setOnClickListener {
            SettingBottomSheetFragment.newInstance(
                preferenceManager.getThemeType() ?: -1
            ) {
                setThemeTextView(it)
                preferenceManager.putThemeType(it)

                if (it == Int.MIN_VALUE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        )
                    } else {
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                        )
                    }
                } else {
                    AppCompatDelegate.setDefaultNightMode(it)
                }

            }.show(supportFragmentManager, SettingBottomSheetFragment.TAG)
        }


        logoutTextView.setOnClickListener {
            showLogoutAlertDialog()
        }

        changePasswordTextView.setOnClickListener {
            showPasswordChangeAlertDialog()
        }

        signOutTextView.setOnClickListener {
            showSighOutAlertDialog()
        }
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }


    private fun setThemeTextView(it: Int) = with(binding) {
        when (it) {
            UiModeManager.MODE_NIGHT_NO -> {
                themeModeTextView.text = getString(R.string.light_mode)
            }

            UiModeManager.MODE_NIGHT_YES -> {
                themeModeTextView.text = getString(R.string.dark_mode)
            }

            else -> themeModeTextView.text = getString(R.string.system_setting_mode)
        }
    }


    private fun showSendEmailAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("인증 메일을 재발송 하시겠어요?")
            .setMessage("( 인증 후 다시 로그인 해주세요! )")
            .setPositiveButton("네") { dialog, _ ->
                viewModel.sendVerifyEmail()
                dialog.dismiss()
            }
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun showLogoutAlertDialog() {
        AlertDialog.Builder(this)
            .setMessage("로그아웃 하시겠어요?")
            .setPositiveButton("네") { dialog, _ ->
                viewModel.logout()
                dialog.dismiss()
            }
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    private fun showPasswordChangeAlertDialog() {
        val currentPasswordEditText = EditText(this).apply {
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(30.fromDpToPx(), 0, 30.fromDpToPx(), 0)
            layoutParams = lp
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            background = null
            hint = "현재 비밀번호"
        }

        val newPasswordEditText = EditText(this).apply {
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(30.fromDpToPx(), 0, 30.fromDpToPx(), 0)
            layoutParams = lp
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            background = null
            hint = "변경할 비밀번호"
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(currentPasswordEditText)
            addView(newPasswordEditText)
        }


        AlertDialog.Builder(this)
            .setMessage("변경할 비밀번호를 입력해주세요\n")
            .setView(container)
            .setPositiveButton("변경") { dialog, _ ->
                val inputString = newPasswordEditText.text.toString()
                if (inputString.length > 8) {
                    viewModel.changePassWord(
                        currentPasswordEditText.text.toString(),
                        newPasswordEditText.text.toString()
                    )
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "8글자 이상의 비밀번호로 변경해주세요", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun showSighOutAlertDialog() {
        val editText = EditText(this).apply {
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(30.fromDpToPx(), 0, 30.fromDpToPx(), 0)
            layoutParams = lp
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            background = null
            hint = "비밀번호"
        }

        val container = FrameLayout(this)
        container.addView(editText)


        AlertDialog.Builder(this)
            .setMessage("회원 탈퇴 하시겠어요?\n\n비밀번호를 입력해주세요\n")
            .setView(container)
            .setPositiveButton("네") { dialog, _ ->
                viewModel.signOut(editText.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java)
    }
}