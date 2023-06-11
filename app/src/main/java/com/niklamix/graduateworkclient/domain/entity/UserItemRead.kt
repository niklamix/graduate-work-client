package com.niklamix.graduateworkclient.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItemRead(
    val id: Long,
    val createdDate: String,
    val lastModifiedDate: String,
    val name: String?,
    val surname: String?,
    val email: String?,
    val country: String?,
    val city: String?,
    val phone: String?,
    val login: String,
    val photo: String?,
    val adminFlag: Boolean,
    val enabled: Boolean,
) : Parcelable
