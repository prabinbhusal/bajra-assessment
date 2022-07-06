package com.bajra.assessment.data.repository

import com.bajra.assessment.utils.UiState

interface AuthRepository {
    fun registerUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
}