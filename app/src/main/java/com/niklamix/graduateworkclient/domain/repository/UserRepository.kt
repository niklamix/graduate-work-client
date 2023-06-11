package com.niklamix.graduateworkclient.domain.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.niklamix.graduateworkclient.domain.entity.UserFilter
import com.niklamix.graduateworkclient.domain.entity.UserItem
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.entity.UserItemWrite

interface UserRepository {
    fun addUserItem(user: UserItemWrite, context: Context?): Boolean

    fun deleteUserItem(user: UserItemRead, context: Context?)

    fun getCurrentUserItem(context: Context?): UserItemRead?

    fun getUserList(page: Int, size: Int, filter: UserFilter, context: Context?): MutableLiveData<List<UserItemRead>>

    fun updateUserItem(userId: Long?, user: UserItemWrite, adminFlag: Boolean, context: Context?): Boolean

    fun changeAdminFlag(user: UserItemRead, context: Context?)

    fun changeEnabledFlag(user: UserItemRead, context: Context?)

    fun updateCredentials(login: String, password: String)
}