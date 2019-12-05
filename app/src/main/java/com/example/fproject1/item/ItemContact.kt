package com.example.fproject1.item

import com.example.fproject1.R
import com.example.fproject1.model.Account
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.custom_contact_list_layout.view.*

class ItemContact(val user : Account) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.custom_contact_list_layout
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.contact_name.text = user.fname
    }
}