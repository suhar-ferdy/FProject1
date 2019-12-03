package com.example.fproject1.adapter

import com.example.fproject1.R
import com.example.fproject1.model.Account
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.custom_layout.view.*


class SettingsOptionAdapter(val user : Account): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        when(position){
            0 ->{
                viewHolder.itemView.settings_textView_title.text = "Display Name"
                viewHolder.itemView.settings_textView_content.text = user.fname
            }
            1 ->{
                viewHolder.itemView.settings_textView_title.text = "ID"
                viewHolder.itemView.settings_textView_content.text = user.userId
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.custom_layout
    }

}
