package com.niklamix.graduateworkclient.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.niklamix.graduateworkclient.data.UserRepositoryImpl
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.usecases.GetCurrentUserItemUseCase
import com.niklamix.graduateworkclient.domain.usecases.UpdateCredentialsUseCase

class UserViewModel : ViewModel() {
    private val repository = UserRepositoryImpl

    private val _errorInputLogin = MutableLiveData<Boolean>()
    val errorInputLogin: LiveData<Boolean>
        get() = _errorInputLogin

    private val _errorInputPassword = MutableLiveData<Boolean>()
    val errorInputPassword: LiveData<Boolean>
        get() = _errorInputPassword

    private val getCurrentUserItemUseCase = GetCurrentUserItemUseCase(repository)
    private val updateCredentialsUseCase = UpdateCredentialsUseCase(repository)

    private val _userItem = MutableLiveData<UserItemRead?>()
    val userItem: LiveData<UserItemRead?>
        get() = _userItem

    fun getCurrentUserItem(context: Context?) {
        val item = getCurrentUserItemUseCase.getCurrentUserItem(context)
        _userItem.value = item
    }

    fun updateCredentials(inputLogin: String, inputPassword: String) {
        val login = parseString(inputLogin)
        val password = parseString(inputPassword)
        if (validateInput(login, password)) {
            updateCredentialsUseCase.updateCredentials(login, password)
        }
    }

    private fun parseString(string: String): String {
        return string.trim()
    }

    private fun validateInput(login: String, password: String): Boolean {
        resetErrorInputLogin()
        resetErrorInputPassword()
        var result = true
        if (login.length < 3) {
            _errorInputLogin.value = true
            result = false
        }
        if (password.length < 5) {
            _errorInputPassword.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputLogin() {
        _errorInputLogin.value = false
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = false
    }
}