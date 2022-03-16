package com.example.suwon_university_community.ui.login.signIn

import android.app.Activity
import android.net.Uri
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.FragmentSignInBinding
import com.example.suwon_university_community.ui.base.BaseFragment
import javax.inject.Inject

class SignInFragment : BaseFragment<SignInViewModel, FragmentSignInBinding>() {


    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: SignInViewModel by viewModels<SignInViewModel> { viewModelFactory }

    override fun getViewBinding(): FragmentSignInBinding =
        FragmentSignInBinding.inflate(layoutInflater)


    private var isMotionAnimatingEnd: Boolean = false
    private var isCompleteSignIn: Boolean = false


    override fun initViews() {
        bindViews()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.signOut()
    }

    override fun observeData() = viewModel.signInStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is SignInState.Loading -> {
                handleLoadingState()
            }
            is SignInState.Success -> {
                handleSuccessState()
            }

            is SignInState.Error -> {
                handleErrorState(it)
            }

            else -> Unit
        }
    }


    private fun handleLoadingState() {
        binding.progressBar.isGone = false
        binding.messageTextView.visibility = View.GONE
        binding.webMailLinkTextView.visibility = View.GONE
    }


    private fun handleSuccessState() {
        binding.progressBar.isGone = true

        Toast.makeText(
            requireContext(),
            getString(R.string.send_email_check_please),
            Toast.LENGTH_SHORT
        ).show()

        binding.messageTextView.apply {
            visibility = View.VISIBLE
            text = getString(R.string.please_check_your_email)
        }
        binding.webMailLinkTextView.visibility = View.VISIBLE
        isCompleteSignIn = true
    }


    private fun handleErrorState(it: SignInState.Error) {
        binding.progressBar.isGone = true

        binding.messageTextView.apply {
            visibility = View.VISIBLE
            text = getString(it.massageId)
        }

        Toast.makeText(requireContext(), getString(it.massageId), Toast.LENGTH_SHORT).show()
    }


    private fun bindViews() = with(binding) {
        nextButton.apply {
            isEnabled = false
            setOnClickListener {
                if (isMotionAnimatingEnd.not()) {
                    moveNextScene()
                } else if (isCompleteSignIn) {
                    Toast.makeText(requireContext(), "회원 가입 완료", Toast.LENGTH_SHORT).show()
                    viewModel.signOut()
                    findNavController().popBackStack()
                }
            }
        }


        backButton.setOnClickListener {
            motionLayout.transitionToEnd()
            motionLayout.transitionToStart()
            isMotionAnimatingEnd = false
        }

        emailEditText.addTextChangedListener {
            checkEmailWithPassword()
        }

        passWordEditText.addTextChangedListener {
            checkPasswordWithEmail()
        }


        passWordCheckEditText.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO && nextButton.isEnabled) {
                    if (isMotionAnimatingEnd.not()) {
                        moveNextScene()
                    }
                    true
                } else {
                    false
                }
            }

            setOnKeyListener { _, keyCode, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)
                    && nextButton.isEnabled
                ) {
                    if (isMotionAnimatingEnd.not()) {
                        moveNextScene()
                    }
                    true
                } else {
                    false
                }
            }

            addTextChangedListener {
                checkPasswordWithEmail()
            }
        }


        sendButton.setOnClickListener {
                viewModel.sendEmail(emailTextView.text.toString(), passWordEditText.text.toString())
        }

        webMailLinkTextView.setOnClickListener {
            openWebMail()
        }
    }




    private fun checkEmailWithPassword() = with(binding) {
        if (emailEditText.length() > 15) {
            if (emailEditText.text.contains("@suwon.ac.kr")) {
                emailErrorTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                if (passWordEditText.length() > 7 && passWordEditText.text.toString() == passWordCheckEditText.text.toString()) {
                    nextButton.isEnabled = true
                    return
                }
            } else {
                emailErrorTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red)
                )
            }
        }
        nextButton.isEnabled = false
    }


    private fun checkPasswordWithEmail() = with(binding) {
        if (passWordEditText.length() > 7 && passWordCheckEditText.length() > 7) {
            if (passWordEditText.text.toString() == passWordCheckEditText.text.toString()) {
                passwordErrorTextView.isGone = true
                if (emailEditText.length() > 15 && emailEditText.text.contains("@suwon.ac.kr")) {
                    nextButton.isEnabled = true
                    return
                }
            } else {
                passwordErrorTextView.visibility = View.VISIBLE
            }
        }
        nextButton.isEnabled = false
    }


    private fun openWebMail() {
        val loginUri = Uri.Builder().scheme("http").authority("mail.suwon.ac.kr")
            .build()

        CustomTabsIntent.Builder().build().also {
            it.launchUrl(requireContext(), loginUri)
        }
    }

    private fun moveNextScene() = with(binding) {
        motionLayout.transitionToStart()
        motionLayout.transitionToEnd()
        emailTextView.text = emailEditText.text
        isMotionAnimatingEnd = true
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val inputManger =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManger.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}