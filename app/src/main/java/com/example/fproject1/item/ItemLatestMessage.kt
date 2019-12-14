package com.example.fproject1.item

import com.example.fproject1.R
import com.example.fproject1.model.Message
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.custom_latest_message.view.*

class ItemLatestMessage(val message: Message) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.custom_latest_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txt_latest_message.text = message.text
        viewHolder.itemView.txt_display_name.text = message.friendName
    }

}