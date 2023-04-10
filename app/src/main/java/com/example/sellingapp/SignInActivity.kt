package com.example.sellingapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sellingapp.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var googleSignInClient:GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)
        firebaseAuth=FirebaseAuth.getInstance()
        //google signin
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient=GoogleSignIn.getClient(this,gso)
        binding.GSignIn.setOnClickListener {
            signInGoogle()
        }

        binding.clkToSignUP.setOnClickListener {
            var intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener {
            var email=binding.emailEt.text.toString()
            var pass=binding.passET.text.toString()


            if(email.isNotEmpty() && pass.isNotEmpty() ){

                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)

                    }
                    else{
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Empty field is not allowed!!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser!=null){
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun  signInGoogle(){
        val signInIntent=googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode== Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account:GoogleSignInAccount?=task.result
            if(account!=null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val intent:Intent=Intent(this,MainActivity::class.java)
                intent.putExtra("email",account.email)
                intent.putExtra("name",account.displayName)
                intent.putExtra("photo",account.photoUrl)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
}