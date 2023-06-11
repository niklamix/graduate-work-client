package com.niklamix.graduateworkclient.domain.usecases

import android.content.Context
import com.niklamix.graduateworkclient.domain.entity.UserItem
import com.niklamix.graduateworkclient.domain.entity.UserItemRead
import com.niklamix.graduateworkclient.domain.repository.UserRepository

class ChangeAdminFlagUseCase(private val userRepository: UserRepository) {

    fun changeAdminFlag(user: UserItemRead, context: Context?) {
        userRepository.changeAdminFlag(user, context)
    }
}