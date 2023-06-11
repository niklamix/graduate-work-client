package com.niklamix.graduateworkclient.domain.usecases

import com.niklamix.graduateworkclient.domain.repository.UserRepository

class UpdateCredentialsUseCase(private val userRepository: UserRepository) {
    fun updateCredentials(login: String, password: String) {
        userRepository.updateCredentials(login, password)
    }
}