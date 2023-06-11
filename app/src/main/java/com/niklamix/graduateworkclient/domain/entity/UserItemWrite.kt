package com.niklamix.graduateworkclient.domain.entity

data class UserItemWrite(
    val name: String?,
    val surname: String?,
    val email: String?,
    val country: String?,
    val city: String?,
    val phone: String?,
    val photo: String?,
    val login: String,
    val password: String
)