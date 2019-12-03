package com.example.fproject1.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.fproject1.R
import com.example.fproject1.login.SignInActivity
import com.example.fproject1.model.Account
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_sign_up.setOnClickListener(this)
    }
    fun signUp(){
        val uname= register_et_email.text.toString()
        val pass = register_et_pass.text.toString()
        val repass = register_et_pass.text.toString()
        if(pass == repass && uname != "" && pass != "" && repass != ""){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(uname,pass)
                .addOnSuccessListener {
                    Toast.makeText(this,"In progress...",Toast.LENGTH_SHORT).show()
                    addUsertoDB()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun addUsertoDB(){
        val uid = FirebaseAuth.getInstance().uid.toString()
        val fname = register_et_first_name.text.toString()
        val lname = register_et_last_name.text.toString()
        val email = register_et_email.text.toString()
        val pass = register_et_pass.text.toString()

        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")
        val user = Account(uid,fname,lname,email,pass,"","")

        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"User Registered",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                overridePendingTransition(0,0)
            }
            .addOnFailureListener {
                Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onClick(v : View){
        when(v.id){
            R.id.btn_sign_up -> signUp()
        }
    }
}
