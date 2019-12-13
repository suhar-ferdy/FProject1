package com.example.fproject1.item

import com.example.fproject1.R
import com.example.fproject1.model.Message
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_log_to.view.*

class ItemChatLogTo(val message: Message) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_log_to
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txt_chat_log_to.text = message.text
    }

}