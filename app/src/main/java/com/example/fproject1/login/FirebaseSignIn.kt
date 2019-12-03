package com.example.fproject1.login

import android.content.Context
import android.content.Intent
import com.example.fproject1.homepage.TutorialActivity
import com.google.firebase.auth.FirebaseAuth


class FirebaseSignIn {

    fun fproSignIn(context: Context, username : String , pass : String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username,pass)
            .addOnSuccessListener {
                val intent = Intent(context,TutorialActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

    }
}