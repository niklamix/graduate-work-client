package com.niklamix.graduateworkclient.domain.usecases

import android.content.Context
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.repository.UserRepository

class GetCurrentUserItemUseCase(private val userRepository: UserRepository) {
    fun getCurrentUserItem(context: Context?): UserItemRead? {
        return userRepository.getCurrentUserItem(context)
    }
}