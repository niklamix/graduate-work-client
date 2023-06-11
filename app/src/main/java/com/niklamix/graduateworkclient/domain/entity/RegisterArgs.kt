package com.niklamix.graduateworkclient.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterArgs(
    val mode: String,
    val userItem: UserItemRead?
) : Parcelable