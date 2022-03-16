package com.example.suwon_university_community.ui.login.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        bindViews()
    }


    private fun bindViews() = with(binding) {

        emailEditText.addTextChangedListener {
            val enable = it?.let { it.length > 8 && passWordEditText.text.length > 8 } ?: false
            loginButton.isEnabled = enable
        }

        passWordEditText.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    Toast.makeText(requireContext(), "IME_ACTION_TEST", Toast.LENGTH_SHORT).show()
                    true
                } else {
                    false
                }
            }

            setOnKeyListener { _, keyCode, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Toast.makeText(requireContext(), "KEY_CODE_TEST", Toast.LENGTH_SHORT).show()
                    true
                } else {
                    false
                }
            }

            addTextChangedListener {
                val enable = it?.let { it.length > 8 && emailEditText.text.length > 8 } ?: false
                loginButton.isEnabled = enable
            }
        }



        loginButton.apply {
            isEnabled = false

            setOnClickListener {

            }
        }


        searchId.setOnClickListener {

        }

        searchPassword.setOnClickListener {

        }

        signIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_SignInFragment)
        }
    }

}