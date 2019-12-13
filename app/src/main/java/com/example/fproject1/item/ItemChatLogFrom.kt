package com.example.fproject1.item

import android.content.Context
import com.example.fproject1.R
import com.example.fproject1.model.Message
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_log_from.view.*

class ItemChatLogFrom(val message: Message) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_log_from
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txt_chat_log_from.text = message.text
    }

}