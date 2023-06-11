package com.niklamix.graduateworkclient.presentation

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.niklamix.graduateworkclient.R

class UserListViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val ivPhoto = view.findViewById<ImageView>(R.id.iv_photo_item)
    val tvName = view.findViewById<TextView>(R.id.tv_user_name_item)
    val tvSurname = view.findViewById<TextView>(R.id.tv_user_surname_item)
    val tvLogin = view.findViewById<TextView>(R.id.tv_user_login_item)
    val tvCountry = view.findViewById<TextView>(R.id.tv_user_country_item)
    val tvCity = view.findViewById<TextView>(R.id.tv_user_city_item)
    val llAdminPanel = view.findViewById<LinearLayout>(R.id.ll_admin_panel_item)
    val btnUpdate = view.findViewById<ImageButton>(R.id.btn_update_profile_item)
    val btnAdminFlag = view.findViewById<ImageButton>(R.id.btn_update_admin_flag_item)
    val btnEnabled = view.findViewById<ImageButton>(R.id.btn_update_enabled_profile_item)
    val btnDelete = view.findViewById<ImageButton>(R.id.btn_delete_profile_item)
}