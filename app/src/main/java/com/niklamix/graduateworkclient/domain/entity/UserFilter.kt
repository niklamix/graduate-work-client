package com.niklamix.graduateworkclient.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserFilter(
    val name: String = "",
    val surname: String = "",
    val login: String = ""
) : Parcelable
