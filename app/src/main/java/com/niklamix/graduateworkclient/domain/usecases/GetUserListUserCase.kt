package com.niklamix.graduateworkclient.domain.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.niklamix.graduateworkclient.domain.entity.UserFilter
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.repository.UserRepository

class GetUserListUserCase(private val userRepository: UserRepository) {

    fun getUserList(page: Int, size: Int, filter: UserFilter, context: Context?): MutableLiveData<List<UserItemRead>> {
        return userRepository.getUserList(page, size, filter, context)
    }
}