package com.niklamix.graduateworkclient.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class UserItem(
    var name: String,
    var surname: String,
    var email: String,
    var country: String,
    var city: String,
    var phone: String,
    var login: String,
    var photo: String,
    var adminFlag: Boolean = false,
    var enabled: Boolean = true,
    val createdDate: LocalDateTime? = null,
    val lastModifiedDate: LocalDateTime? = null,
    val id: Long = 0,
    var password: String? = null,
) : Parcelable {

}
