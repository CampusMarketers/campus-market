package com.example.sellingapp
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//
//class DemoLoginActivity : AppCompatActivity() {
//
//    private lateinit var tvEmail: TextView
//    private lateinit var tvPassword: TextView
//    private lateinit var btnLogin: Button
//    private lateinit var btnSignup: Button
//
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_demo_login)
//
//        tvEmail = findViewById(R.id.tv_signin_email)
//        tvPassword = findViewById(R.id.tv_signin_password)
//        btnLogin = findViewById(R.id.btn_signin_login)
//        btnSignup = findViewById(R.id.btn_signin_register)
//
//        auth = Firebase.auth
//
//        btnLogin.setOnClickListener {
//
//            val sEmail = tvEmail.text.toString()
//            val sPassword = tvPassword.text.toString()
//
//            if (sEmail.isEmpty() || sPassword.isEmpty()) {
//                Toast.makeText(this, "Empty fields are not allowed.", Toast.LENGTH_SHORT).show()
//            } else if (!isValidPattern(sEmail) || !isValidEmail(sEmail)) {
//                Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show()
//            } else {
//                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(sEmail)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val signInMethods =
//                                task.result?.signInMethods ?: emptyList<String>()
//                            if (signInMethods.isNotEmpty()) {
//                                auth.signInWithEmailAndPassword(sEmail, sPassword)
//                                    .addOnCompleteListener(this) { pTask ->
//                                        if (task.isSuccessful) {
//                                            val verification = auth.currentUser?.isEmailVerified
//                                            if (verification == true) {
//                                                updateUI()
//                                            } else {
//                                                Toast.makeText(
//                                                    this,
//                                                    "Please verify your email.",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                            }
//                                        } else {
//                                            Toast.makeText(
//                                                this,
//                                                "${pTask.exception?.message}",
//                                                Toast.LENGTH_SHORT
//                                            )
//                                                .show()
//                                        }
//                                    }
//                            } else {
//                                Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                        } else {
//                            Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//
//            }
//        }
//
//        btnSignup.setOnClickListener {
//            val intent = Intent(this, DemoAuthActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    private fun updateUI() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    private fun isValidPattern(email: String): Boolean {
//        val emailPattern = Regex("^\\w+@gmail\\.com$")
//        return emailPattern.matches(email)
//    }
//
//    private fun isValidEmail(email: String): Boolean {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//    }
//
//    public override fun onStart() {
//        super.onStart()
//
//        val currentUser = auth.currentUser
//        if (currentUser != null && currentUser.isEmailVerified) {
//            updateUI()
//        }
//    }
//
//
//
//}