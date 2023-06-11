package com.niklamix.graduateworkclient.domain.usecases

import android.content.Context
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.repository.UserRepository

class DeleteUserItemUseCase(private val userRepository: UserRepository) {
    fun deleteUserItem(user: UserItemRead, context: Context?) {
        userRepository.deleteUserItem(user, context)
    }
}