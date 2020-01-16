package com.example.fproject1.homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import com.example.fproject1.R
import com.example.fproject1.adapter.SettingsOptionAdapter
import com.example.fproject1.model.Account
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.custom_layout.view.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.title = "Settings"
        supportActionBar!!.setTitle(Html.fromHtml("<font color='#ffffff'>FProject</font>"))

        fetchDataUsers()

        btn_cancel.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        bg_win_setting.setOnClickListener(this)
        form_settings.setOnClickListener(this)
    }

    private fun fetchDataUsers(){
        val currUser = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users")
        val adapter = GroupAdapter<GroupieViewHolder>()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val userData = it.getValue(Account::class.java)
                    if(userData?.uid == currUser){
                        adapter.add(SettingsOptionAdapter(userData!!))
                        adapter.add(SettingsOptionAdapter(userData!!))
                    }
                    adapter.setOnItemClickListener { item, view ->
                        val pos = view.settings_textView_title.text as String
                        val content = view.settings_textView_content.text as String
                        windowEditSettings(pos,content)
                    }

                    rec_view_settings.adapter = adapter
                }
            }
        })
    }

    private fun windowEditSettings(pos : String, content : String){
        edit_window.visibility = View.VISIBLE
        when(pos){
            "Display Name" -> {
                edit_window_title.text = "Enter your Name"
                tutorial_et_win.setText(content)
            }

            "ID" -> {
                edit_window_title.text = "Enter your ID"
                tutorial_et_win.setText(content)
            }
        }
    }

    private fun editWindowGONE(){
        edit_status_msg_txt.visibility = View.GONE
        edit_window.visibility = View.GONE
    }

    private fun saveEditSettings(){
        val currUser = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users")
        val txt = tutorial_et_win.text.toString()
        var same = false
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val userData = it.getValue(Account::class.java)
                    val data : Account? = userData
                    when(edit_window_title.text){
                        "Enter your ID" -> {
                            if(data?.uid != currUser){
                                if(data?.userId == txt){
                                    same = true //found duplicate userId
                                    edit_status_msg_txt.text = "User ID already exist"
                                    edit_status_msg_txt.visibility = View.VISIBLE
                                }
                            }
                        }
                        "Enter your Name" -> null
                    }

                }
                when(edit_window_title.text){
                    "Enter your ID" ->{
                        if(!same){
                            ref.child("/$currUser").child("/userId").setValue("$txt")
                            editWindowGONE()
                        }
                    }
                    "Enter your Name" -> {
                        ref.child("/$currUser").child("/fname").setValue("$txt")
                        editWindowGONE()
                    }
                }
                fetchDataUsers()
            }
        })
    }
    override fun onClick(v : View){
        when(v.id){
            R.id.btn_cancel -> editWindowGONE()
            R.id.bg_win_setting -> editWindowGONE()
            R.id.btn_save -> saveEditSettings()
            R.id.form_add -> edit_window.visibility = View.VISIBLE

        }
    }

}
