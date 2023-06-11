package com.niklamix.graduateworkclient.domain.usecases

import android.content.Context
import com.niklamix.graduateworkclient.domain.entity.UserItem
import com.niklamix.graduateworkclient.domain.entity.UserItemWrite
import com.niklamix.graduateworkclient.domain.repository.UserRepository

class UpdateUserItemUseCase(private val userRepository: UserRepository) {

    fun updateUserItem(userId: Long?, user: UserItemWrite, adminFlag: Boolean, context: Context?): Boolean {
        return userRepository.updateUserItem(userId, user, adminFlag, context)
    }
}