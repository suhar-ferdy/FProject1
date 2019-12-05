package com.example.fproject1.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.fproject1.R
import com.example.fproject1.homepage.TutorialActivity
import com.example.fproject1.item.ItemChatLogFrom
import com.example.fproject1.model.Account
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //val username = intent.getStringExtra(TutorialActivity.USER_KEY)
        val user = intent.getParcelableExtra<Account>(TutorialActivity.USER_KEY)
        supportActionBar?.title = user.fname

        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ItemChatLogFrom())
        adapter.add(ItemChatLogFrom())
        adapter.add(ItemChatLogFrom())
        adapter.add(ItemChatLogFrom())
        adapter.add(ItemChatLogFrom())
        adapter.add(ItemChatLogFrom())
        adapter.add(ItemChatLogFrom())
        adapter.add(ItemChatLogFrom())
        adapter.add(ItemChatLogFrom())
        rv_chat_log.adapter = adapter
        rv_chat_log.scrollToPosition(adapter.itemCount - 1)

    }
}
