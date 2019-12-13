package com.example.fproject1.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View

import com.example.fproject1.R
import com.example.fproject1.homepage.TutorialActivity
import com.example.fproject1.item.ItemChatLogFrom
import com.example.fproject1.item.ItemChatLogTo
import com.example.fproject1.model.Account
import com.example.fproject1.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity(), View.OnClickListener {

    var adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var friend : Account
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        //val username = intent.getStringExtra(TutorialActivity.USER_KEY)
        val user = intent.getParcelableExtra<Account>(TutorialActivity.USER_KEY)
        supportActionBar?.title = user.fname
        supportActionBar!!.setTitle(Html.fromHtml("<font color='#ffffff'>FProject</font>"))
        friend = intent.getParcelableExtra(TutorialActivity.USER_KEY)
        uid = FirebaseAuth.getInstance().uid!!
        rv_chat_log.adapter = adapter
        rv_chat_log.scrollToPosition(adapter.itemCount - 1)
        fetchMessage()

        btn_chat_log_send.setOnClickListener(this)
    }

    private fun sendMessage(){
        val text = et_chat_log.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/messages/$uid/${friend.uid}").push()
        val ref2 = FirebaseDatabase.getInstance().getReference("/messages/${friend.uid}/$uid").push()
        val refLatestMessageMe = FirebaseDatabase.getInstance().getReference("/LatestMessage/$uid/${friend.uid}")
        val refLatestMessageFriend = FirebaseDatabase.getInstance().getReference("/LatestMessage/${friend.uid}/$uid")
        val messageData = Message(uid,friend.uid,text)
        if(text != ""){
            et_chat_log.text = null
            ref.setValue(messageData)
            ref2.setValue(messageData)
            refLatestMessageMe.setValue(messageData)
            refLatestMessageFriend.setValue(messageData)
        }

    }

    private fun fetchMessage(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages/$uid/${friend.uid}")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(Message::class.java)
                Log.d("CHAT","${data?.text}")
                if(data?.fromID == friend.uid)
                    adapter.add(ItemChatLogFrom(data))
                else
                    adapter.add(ItemChatLogTo(data!!))
                rv_chat_log.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    override fun onClick(v : View){
        when(v.id){
            R.id.btn_chat_log_send -> sendMessage()
        }
    }
}
