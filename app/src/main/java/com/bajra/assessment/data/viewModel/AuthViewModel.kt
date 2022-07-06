package com.bajra.assessment.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bajra.assessment.data.repository.AuthRepository
import com.bajra.assessment.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    fun register(
        email: String,
        password: String,
    ) {
        _register.value = UiState.Loading
        repository.registerUser(
            email = email,
            password = password,
        ) { _register.value = it }
    }

    fun login(
        email: String,
        password: String
    ) {
        _login.value = UiState.Loading
        repository.loginUser(
            email,
            password
        ) {
            _login.value = it
        }
    }

    fun logout(result: () -> Unit) {
        repository.logout(result)
    }

}