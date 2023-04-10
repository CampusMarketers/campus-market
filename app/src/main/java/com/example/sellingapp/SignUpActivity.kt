package com.example.sellingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sellingapp.databinding.ActivitySignUpBinding
import com.example.sellingapp.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        binding.clkToSignin.setOnClickListener {
            val intent=Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
        binding.signUp.setOnClickListener{
            var email=binding.emailEt.text.toString()
            var pass=binding.passET.text.toString()
            var confirmPass=binding.confirmPassEt.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if(pass==confirmPass){

                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{

                        if(it.isSuccessful){
                            Toast.makeText(this,"successful",Toast.LENGTH_SHORT).show()

                            database.reference.child("User").child(firebaseAuth.uid.toString()).setValue(
                                UserData(null,email))
                            val intent=Intent(this,SignInActivity::class.java)
                            startActivity(intent)

                        }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Empty field is not allowed!!",Toast.LENGTH_SHORT).show()
            }

        }
    }
}


////
//package com.example.sellingapp
////
////import android.content.Intent
////import android.os.Bundle
////import android.os.Handler
////import android.os.Looper
////import android.text.Editable
////import android.text.TextWatcher
////import android.widget.Button
////import android.widget.EditText
////import android.widget.TextView
////import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
////import com.google.firebase.auth.FirebaseAuth
////import com.google.firebase.auth.ktx.auth
////import com.google.firebase.database.DatabaseReference
////import com.google.firebase.database.ktx.database
////import com.google.firebase.ktx.Firebase
////
//class SignUpActivity : AppCompatActivity() {
////
////    private lateinit var tvName: TextView
////    private lateinit var tvEmail: TextView
////    private lateinit var tvPassword: TextView
////    private lateinit var tvConfPassword: TextView
////    private lateinit var btnRegister: Button
////    private lateinit var btnLogin: Button
////
////    private lateinit var auth: FirebaseAuth
////    private lateinit var database: DatabaseReference
////
////    private var handler: Handler? = null
////    private val unverifiedUserDeleteTime = 2 * 60 * 1000
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_demo_auth)
////
////        tvName = findViewById(R.id.tv_signup_name)
////        tvEmail = findViewById(R.id.tv_signup_email)
////        tvPassword = findViewById(R.id.tv_signup_password)
////        tvConfPassword = findViewById(R.id.tv_signup_confirm_password)
////        btnRegister = findViewById(R.id.btn_signup_register)
////        btnLogin = findViewById(R.id.btn_signup_login)
////
////        auth = Firebase.auth
////        database = Firebase.database.reference
////
////        val emailField: EditText = findViewById(R.id.tv_signup_email)
////        val passwordField: EditText = findViewById(R.id.tv_signup_password)
////        val confirmPassField: EditText = findViewById(R.id.tv_signup_confirm_password)
////
////        emailField.addTextChangedListener(object : TextWatcher {
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////                if (count > 0 && s?.get(start + count - 1) == ' ') {
////                    // Remove the space
////                    val newEmail = StringBuilder(s).deleteCharAt(start + count - 1).toString()
////                    emailField.setText(newEmail)
////                    // Move cursor to correct position
////                    emailField.setSelection(start + count - 1)
////                } else {
////                    tvEmail.text = s.toString()
////                }
////            }
////
////            override fun afterTextChanged(s: Editable?) {}
////        })
////
////        passwordField.addTextChangedListener(object : TextWatcher {
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////                if (count > 0 && s?.get(start + count - 1) == ' ') {
////                    // Remove the space
////                    val newEmail = StringBuilder(s).deleteCharAt(start + count - 1).toString()
////                    passwordField.setText(newEmail)
////                    // Move cursor to correct position
////                    passwordField.setSelection(start + count - 1)
////                } else {
////                    tvPassword.text = s.toString()
////                }
////            }
////
////            override fun afterTextChanged(s: Editable?) {}
////        })
////
////        confirmPassField.addTextChangedListener(object : TextWatcher {
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////                if (count > 0 && s?.get(start + count - 1) == ' ') {
////                    // Remove the space
////                    val newEmail = StringBuilder(s).deleteCharAt(start + count - 1).toString()
////                    confirmPassField.setText(newEmail)
////                    // Move cursor to correct position
////                    confirmPassField.setSelection(start + count - 1)
////                } else {
////                    tvConfPassword.text = s.toString()
////                }
////            }
////
////            override fun afterTextChanged(s: Editable?) {}
////        })
////
////        btnRegister.setOnClickListener {
////
////            val sName = tvName.text.toString()
////            val sEmail = tvEmail.text.toString()
////            val sPassword = tvPassword.text.toString()
////            val sConfPassword = tvConfPassword.text.toString()
////
////            if (sName.isEmpty() || sEmail.isEmpty() || sPassword.isEmpty() || sConfPassword.isEmpty()) {
////                Toast.makeText(this, "Empty fields are not allowed.", Toast.LENGTH_SHORT).show()
////            } else if (sPassword != sConfPassword) {
////                Toast.makeText(this, "Password confirmation does not match.", Toast.LENGTH_SHORT)
////                    .show()
////            } else if (isValidPattern(sEmail) && isValidEmail(sEmail)) {
////                Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show()
////            } else {
////                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(sEmail)
////                    .addOnCompleteListener { task ->
////                        if (task.isSuccessful) {
////                            val signInMethods =
////                                task.result?.signInMethods ?: emptyList<String>()
////                            if (signInMethods.isNotEmpty()) {
////                                Toast.makeText(this, "Email already exists.", Toast.LENGTH_SHORT)
////                                    .show()
////                            } else {
////                                auth.createUserWithEmailAndPassword(sEmail, sPassword)
////                                    .addOnCompleteListener(this) { task ->
////                                        if (task.isSuccessful) {
////                                            val user = auth.currentUser
////                                            user?.sendEmailVerification()
////                                                ?.addOnCompleteListener { verificationTask ->
////                                                    if (verificationTask.isSuccessful) {
////                                                        Toast.makeText(
////                                                            this,
////                                                            "A verification link has been sent to your email account.",
////                                                            Toast.LENGTH_SHORT
////                                                        ).show()
////                                                        saveData()
////                                                        // Initialize the handler
////                                                        handler = Handler(Looper.getMainLooper())
////
////                                                        // Call the auth state listener initially
////                                                        auth.addAuthStateListener(authStateListener)
////
////                                                        // Schedule the periodic task to call the auth state listener
////                                                        handler?.postDelayed(
////                                                            periodicAuthStateListener,
////                                                            0L
////                                                        )
////                                                    } else {
////                                                        Toast.makeText(
////                                                            this,
////                                                            "${verificationTask.exception?.message}",
////                                                            Toast.LENGTH_SHORT
////                                                        ).show()
////                                                    }
////                                                }
////                                        } else {
////                                            Toast.makeText(
////                                                this,
////                                                "${task.exception?.message}",
////                                                Toast.LENGTH_LONG
////                                            ).show()
////                                        }
////                                    }
////                            }
////                        } else {
////                            Toast.makeText(
////                                this,
////                                "${task.exception?.message}",
////                                Toast.LENGTH_SHORT
////                            ).show()
////                        }
////                    }
////            }
////        }
////
////        btnLogin.setOnClickListener {
////            val intent = Intent(this, DemoLoginActivity::class.java)
////            startActivity(intent)
////        }
////    }
////
////    private fun saveData() {
////        val sEmail = tvEmail.text.toString()
////        val sName = tvName.text.toString()
////
////        val user = UserModel(sName, sEmail)
////        val userID = FirebaseAuth.getInstance().currentUser!!.uid
////
////        database.child("User").child(userID).setValue(user)
////    }
////
////    private fun isValidPattern(email: String): Boolean {
////        val emailPattern = Regex("^\\w+@gmail\\.com$")
////        return emailPattern.matches(email)
////    }
////
////    private fun isValidEmail(email: String): Boolean {
////        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
////    }
////
////    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
////        val user = firebaseAuth.currentUser
////
////        if ((user != null) && !user.isEmailVerified) {
////            val creationTime = user.metadata?.creationTimestamp ?: 0
////            val currentTime = System.currentTimeMillis()
////
////            if (currentTime - creationTime >= unverifiedUserDeleteTime) {
////                user.delete()
////                    .addOnCompleteListener { task ->
////                        if (task.isSuccessful) {
////                            Toast.makeText(
////                                this,
////                                "Verification failed",
////                                Toast.LENGTH_SHORT
////                            )
////                                .show()
////                        } else {
////                            Toast.makeText(
////                                this,
////                                "${task.exception?.message}",
////                                Toast.LENGTH_SHORT
////                            ).show()
////                        }
////                    }
////            }
////        }
////    }
////
////    private val periodicAuthStateListener = object : Runnable {
////        override fun run() {
////            auth.addAuthStateListener(authStateListener)
////            handler?.postDelayed(this, 1000L)
////        }
////    }
////
////    override fun onDestroy() {
////        super.onDestroy()
////
////        auth.removeAuthStateListener(authStateListener)
////        handler?.removeCallbacks(periodicAuthStateListener)
////    }
//}
