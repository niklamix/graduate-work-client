package com.niklamix.graduateworkclient.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.niklamix.graduateworkclient.data.UserRepositoryImpl
import com.niklamix.graduateworkclient.domain.entity.UserFilter
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.usecases.ChangeAdminFlagUseCase
import com.niklamix.graduateworkclient.domain.usecases.ChangeEnabledFlagUseCase
import com.niklamix.graduateworkclient.domain.usecases.DeleteUserItemUseCase
import com.niklamix.graduateworkclient.domain.usecases.GetUserListUserCase

class SearchUserViewModel : ViewModel() {
    private val repository = UserRepositoryImpl

    private val getUserListUserCase = GetUserListUserCase(repository)
    private val deleteUserItemUseCase = DeleteUserItemUseCase(repository)
    private val changeAdminFlagUseCase = ChangeAdminFlagUseCase(repository)
    private val changeEnabledFlagUseCase = ChangeEnabledFlagUseCase(repository)

    private var _userList = MutableLiveData<List<UserItemRead>>()
    val userList: LiveData<List<UserItemRead>>
        get() = _userList

    fun getUserList(
        page: Int,
        size: Int,
        filter: UserFilter,
        context: Context?
    ) {
        _userList = getUserListUserCase.getUserList(page, size, filter, context)
    }

    fun deleteUser(userItemRead: UserItemRead, context: Context?) {
        deleteUserItemUseCase.deleteUserItem(userItemRead, context)
    }

    fun changeAdminFlag(userItemRead: UserItemRead, context: Context?) {
        changeAdminFlagUseCase.changeAdminFlag(userItemRead, context)
    }

    fun changeEnabledFlag(userItemRead: UserItemRead, context: Context?) {
        changeEnabledFlagUseCase.changeEnabledFlag(userItemRead, context)
    }
}