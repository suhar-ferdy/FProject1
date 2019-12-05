package com.example.fproject1.item

import com.example.fproject1.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ItemChatLogTo : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_log_to
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}