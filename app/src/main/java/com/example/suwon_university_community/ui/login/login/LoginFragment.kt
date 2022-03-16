package com.example.suwon_university_community.ui.login.login

import android.app.Activity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.FragmentLoginBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {


    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: LoginViewModel by viewModels<LoginViewModel> { viewModelFactory }


    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)


    override fun initViews() {
        bindViews()
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


        searchId.setOnClickListener {
            // TODO: 아이디찾기
        }

        searchPassword.setOnClickListener {
            // TODO: 비밀번호 찾기
        }

        signIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_SignInFragment)
        }
    }

}