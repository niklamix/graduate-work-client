package com.niklamix.graduateworkclient.presentation.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.niklamix.graduateworkclient.data.UserRepositoryImpl
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.entity.UserItemWrite
import com.niklamix.graduateworkclient.domain.usecases.AddUserItemUseCase
import com.niklamix.graduateworkclient.domain.usecases.GetCurrentUserItemUseCase
import com.niklamix.graduateworkclient.domain.usecases.UpdateCredentialsUseCase
import com.niklamix.graduateworkclient.domain.usecases.UpdateUserItemUseCase

class UserChangeViewModel : ViewModel() {
    private val repository = UserRepositoryImpl

    private val _updateSuccessful = MutableLiveData<Boolean>()
    val updateSuccessful: LiveData<Boolean>
        get() = _updateSuccessful

    private val _addSuccessful = MutableLiveData<Boolean>()
    val addSuccessful: LiveData<Boolean>
        get() = _addSuccessful

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputSurname = MutableLiveData<Boolean>()
    val errorInputSurname: LiveData<Boolean>
        get() = _errorInputSurname

    private val _errorInputEmail = MutableLiveData<Boolean>()
    val errorInputEmail: LiveData<Boolean>
        get() = _errorInputEmail

    private val _errorInputCountry = MutableLiveData<Boolean>()
    val errorInputCountry: LiveData<Boolean>
        get() = _errorInputCountry

    private val _errorInputCity = MutableLiveData<Boolean>()
    val errorInputCity: LiveData<Boolean>
        get() = _errorInputCity

    private val _errorInputPhone = MutableLiveData<Boolean>()
    val errorInputPhone: LiveData<Boolean>
        get() = _errorInputPhone

    private val _errorInputLogin = MutableLiveData<Boolean>()
    val errorInputLogin: LiveData<Boolean>
        get() = _errorInputLogin

    private val _errorInputPassword = MutableLiveData<Boolean>()
    val errorInputPassword: LiveData<Boolean>
        get() = _errorInputPassword

    private val _shouldCloseScreen = MutableLiveData<Boolean>()
    val shouldCloseScreen: LiveData<Boolean>
        get() = _shouldCloseScreen

    private val addUserItemUseCase = AddUserItemUseCase(repository)
    private val updateUserItemUseCase = UpdateUserItemUseCase(repository)
    private val getCurrentUserItemUseCase = GetCurrentUserItemUseCase(repository)
    private val updateCredentialsUseCase = UpdateCredentialsUseCase(repository)

    fun updateUserItem(userItemWrite: UserItemWrite, context: Context?, adminFlag: Boolean, userId: Long? = null) {
        var isSuccessful = false
        if (validateUser(userItemWrite)) {
            isSuccessful = updateUserItemUseCase.updateUserItem(userId, userItemWrite, adminFlag, context)
            if (!adminFlag && isSuccessful) {
                updateCredentialsUseCase.updateCredentials(userItemWrite.login, userItemWrite.password)
            }
        }
        _updateSuccessful.value = isSuccessful
    }

    fun addUserItem(userItemWrite: UserItemWrite, context: Context?) {
        var isSuccessful = false
        if (validateUser(userItemWrite)) {
            updateCredentialsUseCase.updateCredentials("admin", "admin")
            isSuccessful = addUserItemUseCase.addUserItem(userItemWrite, context)
            updateCredentialsUseCase.updateCredentials(userItemWrite.login, userItemWrite.password)
        }
        _addSuccessful.value = isSuccessful
    }
    fun getUpdatedCurrentUser(context: Context?): UserItemRead? {
        return getCurrentUserItemUseCase.getCurrentUserItem(context)
    }
    private fun validateUser(userItemWrite: UserItemWrite): Boolean {
        var result = true
        val phoneRegex = Regex("^\\+?[1-9]\\d{3,14}\$")
        if (userItemWrite.name?.length!! < 2) {
            _errorInputName.value = true
            result = false
        }
        if (userItemWrite.surname?.length!! < 3) {
            _errorInputSurname.value = true
            result = false
        }
        if (userItemWrite.email?.length!! < 3 ||
            !Patterns.EMAIL_ADDRESS.matcher(userItemWrite.email).matches()) {
            _errorInputEmail.value = true
            result = false
        }
        if (userItemWrite.country?.length!! < 2) {
            _errorInputCountry.value = true
            result = false
        }
        if (userItemWrite.city?.length!! < 3) {
            _errorInputCity.value = true
            result = false
        }
        if (!phoneRegex.matches(userItemWrite.phone!!)) {
            _errorInputPhone.value = true
            result = false
        }
        if (userItemWrite.login.length < 3) {
            _errorInputLogin.value = true
            result = false
        }
        if (userItemWrite.password.length < 5) {
            _errorInputPassword.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputSurname() {
        _errorInputSurname.value = false
    }
    fun resetErrorInputEmail() {
        _errorInputEmail.value = false
    }

    fun resetErrorInputCountry() {
        _errorInputCountry.value = false
    }
    fun resetErrorInputCity() {
        _errorInputCity.value = false
    }

    fun resetErrorInputPhone() {
        _errorInputPhone.value = false
    }
    fun resetErrorInputLogin() {
        _errorInputLogin.value = false
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = true
    }

}