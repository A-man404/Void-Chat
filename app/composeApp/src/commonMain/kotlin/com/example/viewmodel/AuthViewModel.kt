package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)

            val result = AuthRepository.userLogin(email, password)

            _authState.value = if (result.isSuccess) {
                val repositoryResponse = result.getOrNull()
                _authState.value.copy(
                    isLoading = false,
                    message = repositoryResponse?.message,
                    data = repositoryResponse?.data,
                    errorMessage = null
                )
            } else {
                _authState.value.copy(
                    isLoading = false,
                    message = result.getOrNull()?.message,
                    errorMessage = result.exceptionOrNull()?.message,
                    data = null
                )
            }
        }
    }

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)

            val result = AuthRepository.registerUser(name, email, password)

            _authState.value = if (result.isSuccess) {
                val repositoryResponse = result.getOrNull()
                _authState.value.copy(
                    isLoading = false,
                    message = repositoryResponse?.message,
                    data = repositoryResponse?.data,
                    errorMessage = null
                )
            } else {
                _authState.value.copy(
                    isLoading = false,
                    message = result.getOrNull()?.message,
                    errorMessage = result.exceptionOrNull()?.message,
                    data = null
                )
            }
        }
    }
}

data class AuthState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val data: Any? = null
)
