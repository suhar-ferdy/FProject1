package com.example.fproject1.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.fproject1.R
import com.example.fproject1.homepage.TutorialActivity
import com.example.fproject1.register.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
        buttonGoogle.setOnClickListener(this)
        buttonFacebook.setOnClickListener(this)
        buttonFPro.setOnClickListener(this)
        text_register_clickable.setOnClickListener(this)

    }
    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Toast.makeText(this,"$currentUser",Toast.LENGTH_SHORT).show()
        updateUI(currentUser)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                updateUI(null)

            }
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }

            }
    }

    private fun signIn(authWith : String) {
        if(authWith == "google"){
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        else if(authWith == "fpro"){
            val uname = login_et_email.text.toString()
            val pass = login_et_pass.text.toString()
            if(pass != "" && uname != "")
                FirebaseSignIn().fproSignIn(this,uname,pass)
            else
                Toast.makeText(this,"Not Available Yet", Toast.LENGTH_SHORT).show()
        }
        else if(authWith == "facebook"){
            Toast.makeText(this,"Not Available Yet", Toast.LENGTH_SHORT).show()
        }

    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, TutorialActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {

        }
    }

    private fun disrectToRegisterPage(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.buttonGoogle -> signIn("google")
            R.id.buttonFacebook -> signIn("facebook")
            R.id.buttonFPro -> signIn("fpro")
            R.id.text_register_clickable ->  disrectToRegisterPage()
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001

    }
}
