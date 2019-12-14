package com.example.fproject1.homepage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.fproject1.R
import com.example.fproject1.item.ItemContact
import com.example.fproject1.adapter.PagerAdapter
import com.example.fproject1.chat.ChatLogActivity
import com.example.fproject1.item.ItemLatestMessage
import com.example.fproject1.login.SignInActivity
import com.example.fproject1.model.Account
import com.example.fproject1.model.Message
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_tutorial.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_friend_list.*

@SuppressLint("RestrictedApi")
class TutorialActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var my_user : Account
    var floatOption : Int = 0

    companion object{
        val USER_KEY = "USER_KEY"
        val MY_KEY = "MY_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        actionBarSettings()
        changeFloatingButtonImg()
        fetchDataUsers()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        val adapter = PagerAdapter(supportFragmentManager)
        viewpager.adapter = adapter
        tab_layout.setupWithViewPager(viewpager)

        bg_win_add.setOnClickListener(this)
        form_add.setOnClickListener(this)
        floating_action_button.setOnClickListener(this)
    }

    //load fragment content
    private fun fetchDataUsers(){
        val currUser = FirebaseAuth.getInstance().uid
        val refContact = FirebaseDatabase.getInstance().getReference("/Users")
        val refLatestmessage = FirebaseDatabase.getInstance().getReference("/LatestMessage/$currUser")
        val adapterContact = GroupAdapter<GroupieViewHolder>()
        val adapterLatestMessage = GroupAdapter<GroupieViewHolder>()
        //load user contact
        refContact.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val userData = it.getValue(Account::class.java)
                    if(userData?.uid == currUser){
                        my_user = userData!!
                    }
                    else{
                        adapterContact.add(ItemContact(userData!!))
                    }
                    adapterContact.setOnItemClickListener{item, view ->
                        val userItem = item as ItemContact
                        val intent = Intent(view.context, ChatLogActivity::class.java)
                        intent.putExtra(USER_KEY,userItem?.user)
                        intent.putExtra(MY_KEY,my_user)
                        startActivity(intent)

                    }
                    rv_contact_list.adapter = adapterContact
                }
            }
        })

        //load user latest message
        refLatestmessage.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val userMessage = it.getValue(Message::class.java)
                    adapterLatestMessage.add(ItemLatestMessage(userMessage!!))
                }
                adapterLatestMessage.setOnItemClickListener { item, view ->
                    val userItem = item as ItemLatestMessage
                    val uid = FirebaseAuth.getInstance().uid
                    var fromID = ""
                    if(uid == userItem.message.fromID)
                        fromID = userItem.message.toID
                    else
                        fromID = userItem.message.fromID

                    refContact.child("/$fromID").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val intent = Intent(this@TutorialActivity,ChatLogActivity::class.java)
                            val userData = p0.getValue(Account::class.java)
                            intent.putExtra(USER_KEY,userData)
                            intent.putExtra(MY_KEY,my_user)
                            startActivity(intent)
                        }


                    })
                }

                rv_latest_message.adapter = adapterLatestMessage
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_sign_out  -> signOut()
            R.id.action_settings -> settings()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSettings(){
        supportActionBar!!.elevation = 0f
        supportActionBar!!.setTitle(Html.fromHtml("<font color='#ffffff'>FProject</font>"))
    }
    private fun settings(){
        val intent = Intent(this,SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun changeFloatingButtonImg(){
        viewpager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                if(position == 0){
                    floating_action_button.setImageResource(R.drawable.ic_action_chat)
                    floatOption = 0
                }
                else{
                    floating_action_button.setImageResource(R.drawable.ic_action_add)
                    floatOption = 1
                }

            }

        })
    }

    override fun onClick(v : View){
        when(v.id){
            R.id.floating_action_button -> showAddContactWindow(floatOption)
            R.id.bg_win_add -> hideAddContactWindow()
            R.id.form_add -> showAddContactWindow(floatOption)
        }
    }

    private fun showAddContactWindow(pos : Int){
        when(pos){
            0 -> null
            1 -> {
                add_friend_window.visibility = View.VISIBLE
                floating_action_button.visibility = View.GONE
            }

        }
    }

    private fun hideAddContactWindow(){
        add_friend_window.visibility = View.GONE
        floating_action_button.visibility = View.VISIBLE
    }

}
