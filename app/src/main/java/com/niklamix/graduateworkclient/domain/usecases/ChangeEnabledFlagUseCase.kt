package com.niklamix.graduateworkclient.domain.usecases

import android.content.Context
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.repository.UserRepository

class ChangeEnabledFlagUseCase(private val userRepository: UserRepository) {

    fun changeEnabledFlag(user: UserItemRead, context: Context?) {
        userRepository.changeEnabledFlag(user, context)
    }
}