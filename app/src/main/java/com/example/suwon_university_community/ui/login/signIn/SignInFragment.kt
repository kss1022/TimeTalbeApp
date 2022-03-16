package com.example.suwon_university_community.ui.login.signIn

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment() {


    private val auth: FirebaseAuth by lazy { Firebase.auth }

    private lateinit var binding: FragmentSignInBinding

    private var isMotionAnimatingEnd: Boolean = false
    private var isCompleteSignIn: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        bindViews()
    }

    private fun bindViews() = with(binding) {
        nextButton.apply {
            isEnabled = false
            setOnClickListener {
                if (isMotionAnimatingEnd.not()) {
                    motionLayout.transitionToStart()
                    motionLayout.transitionToEnd()
                    emailTextView.text = emailEditText.text
                    isMotionAnimatingEnd = true
                } else if (isCompleteSignIn) {
                    Toast.makeText(requireContext(), "회원 가입 완료", Toast.LENGTH_SHORT).show()
                    auth.signOut()
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

        passWordCheckEditText.addTextChangedListener {
            checkPasswordWithEmail()
        }


        sendButton.setOnClickListener {
            val email = emailTextView.text
            val password = passWordEditText.text

            sendEmail(email.toString(), password.toString())
        }

        webMailLinkTextView.setOnClickListener {
            openWebMail()
        }
    }


    private fun checkEmailWithPassword() = with(binding) {
        if (emailEditText.length() > 15) {
            if (emailEditText.text.contains("@suwon.ac.kr")) {
                emailErrorTextView.isGone = true
                if (passWordEditText.length() > 7 && passWordEditText.text.toString() == passWordCheckEditText.text.toString()) {
                    nextButton.isEnabled = true
                    return
                }
            } else {
                emailErrorTextView.visibility = View.VISIBLE
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


    private fun sendEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.setLanguageCode("ko")
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.send_email_check_please),
                                    Toast.LENGTH_SHORT
                                ).show()

                                binding.sendMessageTextView.visibility = View.VISIBLE
                                binding.webMailLinkTextView.visibility = View.VISIBLE
                                isCompleteSignIn = true
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.email_send_failed_check_your_email),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "인증 메일이 전송되지 않았어요 이메일을 확인해주세요😭",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun openWebMail() {
        val loginUri = Uri.Builder().scheme("http").authority("mail.suwon.ac.kr")
            .build()

        CustomTabsIntent.Builder().build().also {
            it.launchUrl(requireContext(), loginUri)
        }
    }


}