package com.example.fproject1.memo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.fproject1.R
import com.example.fproject1.homepage.TutorialActivity
import com.example.fproject1.model.LiveEditorMember
import com.example.fproject1.model.LiveEditorRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_memo.*

class MemoActivity : AppCompatActivity() {

    private lateinit var roomID : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
        actionBarSettings()
        roomID = intent.getStringExtra(TutorialActivity.ROOM_KEY)
        val ref = FirebaseDatabase.getInstance().getReference("/LiveEditor/Room/$roomID/")
        val ref2 = FirebaseDatabase.getInstance().getReference("/LiveEditor/Member/$roomID")
        var editable = "no"
        val uid = FirebaseAuth.getInstance().uid
        loadData()
        ref2.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val data = it.getValue(LiveEditorMember::class.java)
                    if(data?.job == "host" && data?.uid == uid)
                        editable = "yes"
                }
            }
        })


        et_memo.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(editable == "yes"){
                    et_memo.isEnabled = true
                    val data = LiveEditorRoom(roomID,et_memo.text.toString())
                    ref.child("text").setValue("${data.text}")
                }
                else{
                    et_memo.isEnabled = false
                    et_memo.setTextColor(Color.BLACK)
                    loadData()
                }
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_memo,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSettings(){
        supportActionBar!!.setTitle(Html.fromHtml("<font color='#ffffff'>Live Share Editor</font>"))
    }

    private fun loadData(){
        val ref = FirebaseDatabase.getInstance().getReference("/LiveEditor/Room/$roomID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val data = p0.getValue(LiveEditorRoom::class.java)
                Log.d("MeMo","${data?.text}")
                et_memo.setText(data?.text)
            }
        })

    }

}
