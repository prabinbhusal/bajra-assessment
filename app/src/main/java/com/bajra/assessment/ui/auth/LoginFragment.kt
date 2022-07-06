package com.bajra.assessment.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bajra.assessment.R
import com.bajra.assessment.data.viewModel.AuthViewModel
import com.bajra.assessment.databinding.FragmentLoginBinding
import com.bajra.assessment.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()
        setObservers()
    }

    private fun setViews() {

        binding.buttonLogin.setOnClickListener {
            if (validation()) {
                authViewModel.login(
                    email = binding.editTextEmail.text.toString(),
                    password = binding.editTextPassword.text.toString()
                )
            }
        }

        binding.buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setObservers() {
        authViewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.show()
                }
                is UiState.Failure -> {
                    binding.progress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progress.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_loginFragment_to_nav_home)
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.editTextEmail.text.isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.enter_email))
        } else {
            if (!binding.editTextEmail.text.toString().isValidEmail()) {
                isValid = false
                toast(getString(R.string.invalid_email))
            }
        }
        if (binding.editTextPassword.text.isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.enter_password))
        } else {
            if (binding.editTextPassword.text.toString().length < 8) {
                isValid = false
                toast(getString(R.string.invalid_password))
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}