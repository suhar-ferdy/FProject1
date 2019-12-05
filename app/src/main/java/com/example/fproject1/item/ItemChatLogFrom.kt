package com.example.fproject1.item

import android.content.Context
import com.example.fproject1.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ItemChatLogFrom : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_log_from
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}