package com.niklamix.graduateworkclient.presentation

import androidx.recyclerview.widget.DiffUtil
import com.niklamix.graduateworkclient.domain.entity.UserItemRead

class UserListDiffCallback: DiffUtil.ItemCallback<UserItemRead>() {
    override fun areItemsTheSame(oldItem: UserItemRead, newItem: UserItemRead): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserItemRead, newItem: UserItemRead): Boolean {
        return oldItem == newItem
    }
}