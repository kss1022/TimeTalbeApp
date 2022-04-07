package com.example.suwon_university_community.ui.login.login

import android.app.Activity
import android.app.AlertDialog
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.databinding.FragmentLoginBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {


    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: LoginViewModel by viewModels<LoginViewModel> { viewModelFactory }

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun initViews() {
        bindViews()
        preferenceManager.getRecentlyLoginId()?.let {
            binding.emailEditText.setText(it)
        }

    }

    override fun observeData() = viewModel.loginStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is LogInState.Loading -> {
                handleLoadingState()
            }
            is LogInState.Success -> {
                handleSuccessState(it)
            }
            is LogInState.Error -> {
                handleErrorState(it)
            }
            else -> Unit
        }
    }

    private fun handleLoadingState() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun handleSuccessState(logInState: LogInState.Success) {
        binding.progressBar.isGone = true

        if (logInState.verified) {
            Toast.makeText(requireContext(), R.string.login_success, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), R.string.email_is_not_verified, Toast.LENGTH_SHORT)
                .show()
        }

        activity?.let {
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    private fun handleErrorState(it: LogInState.Error) {
        binding.progressBar.isGone = true

        Toast.makeText(requireContext(), it.massageId, Toast.LENGTH_SHORT).show()
    }


    private fun bindViews() = with(binding) {

        emailEditText.addTextChangedListener {
            val enable =
                it?.let { emailEditText.length() > 15 && passWordEditText.length() > 7 } ?: false
            loginButton.isEnabled = enable
        }

        passWordEditText.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO  && loginButton.isEnabled) {
                    viewModel.login(emailEditText.text.toString(),  passWordEditText.text.toString())
                    true
                } else {
                    false
                }
            }

            setOnKeyListener { _, keyCode, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)
                    && loginButton.isEnabled) {
                    viewModel.login(emailEditText.text.toString(),  passWordEditText.text.toString())
                    true
                } else {
                    false
                }
            }

            addTextChangedListener {
                val enable =
                    it?.let { passWordEditText.length() > 7 && emailEditText.length() > 15 }
                        ?: false
                loginButton.isEnabled = enable
            }
        }


        loginButton.apply {
            isEnabled = false

            setOnClickListener {
                viewModel.login(emailEditText.text.toString(),  passWordEditText.text.toString())
            }
        }


        searchPassword.setOnClickListener {
            showPasswordResetAlertDialog()
        }

        signIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_SignInFragment)
        }
    }

    private fun showPasswordResetAlertDialog() {
        val editText = EditText(requireContext()).apply {
            val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            lp.setMargins(30.fromDpToPx(), 0, 30.fromDpToPx(), 0)
            layoutParams = lp
        }

        val container = FrameLayout(requireContext())
        container.addView( editText)

        AlertDialog.Builder(requireContext())
            .setTitle("회원가입 한 아이디를 입력해주세요\n")
            .setMessage("비밀번호 재설정 이메일을 전송하시겠어요?")
            .setView(container)
            .setPositiveButton("이메일 발송"){ dialog, _->
                viewModel.sendPasswordResetEmail(editText.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("취소"){ dialog, _->
                dialog.dismiss()
            }
            .show()
    }

}