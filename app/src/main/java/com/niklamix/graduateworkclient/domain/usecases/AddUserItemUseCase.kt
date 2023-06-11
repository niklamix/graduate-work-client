package com.niklamix.graduateworkclient.domain.usecases

import android.content.Context
import com.niklamix.graduateworkclient.domain.entity.UserItem
import com.niklamix.graduateworkclient.domain.entity.UserItemWrite
import com.niklamix.graduateworkclient.domain.repository.UserRepository

class AddUserItemUseCase(private val userRepository: UserRepository) {
    fun addUserItem(user: UserItemWrite, context: Context?): Boolean {
       return userRepository.addUserItem(user, context)
    }
}