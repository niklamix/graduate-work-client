package com.niklamix.graduateworkclient.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.niklamix.graduateworkclient.R
import com.niklamix.graduateworkclient.domain.entity.UserItemRead

class UserListAdapter(private val adminFlag: Boolean): ListAdapter<UserItemRead, UserListViewHolder>(UserListDiffCallback()) {

    lateinit var onUserItemClickListener: (UserItemRead) -> Unit
    lateinit var onUpdateUserItemClickListener: (UserItemRead) -> Unit
    lateinit var onUpdateAdminFlagClickListener: (UserItemRead) -> Unit
    lateinit var onUpdateEnabledFlagClickListener: (UserItemRead) -> Unit
    lateinit var onDeleteUserItemClickListener: (UserItemRead) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val layout = R.layout.item_user
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val userItem = getItem(position)
        holder.tvName.text = userItem.name
        holder.tvSurname.text = userItem.surname
        holder.tvLogin.text = userItem.login
        holder.tvCountry.text = userItem.country
        holder.tvCity.text = userItem.city
        holder.ivPhoto.setImageBitmap(convertBase64InImage(userItem.photo))
        holder.itemView.setOnClickListener {
            onUserItemClickListener(userItem)
        }
        if (adminFlag) {
            holder.llAdminPanel.visibility = View.VISIBLE
            holder.btnUpdate.setOnClickListener {
                onUpdateUserItemClickListener(userItem)
            }
            holder.btnAdminFlag.setOnClickListener {
                onUpdateAdminFlagClickListener(userItem)
            }
            holder.btnEnabled.setOnClickListener {
                onUpdateEnabledFlagClickListener(userItem)
            }
            holder.btnDelete.setOnClickListener {
                onDeleteUserItemClickListener(userItem)
            }
        }
    }

    private fun convertBase64InImage(image: String?): Bitmap? {
        if (image != null) {
            val decodedBytes: ByteArray = Base64.decode(image, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
        return null
    }
}