package com.example.sellingapp

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.sellingapp.databinding.ActivityNavHeaderBinding
import com.example.sellingapp.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class NavHeaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavHeaderBinding
    private var imageUrl: Uri? = null
    private lateinit var dialog: Dialog
    private lateinit var database: DatabaseReference


    override fun onResume() {
        super.onResume()

        val category = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, category)
        binding.txtCategory.setAdapter(arrayAdapter)

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNavHeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)



        database = Firebase.database.reference

        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)


        binding.backButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.profileImage.setOnClickListener {

            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }

        binding.addButton.setOnClickListener {

            if (binding.tvProfileName.text.toString() != ""  && imageUrl != null) {
                dialog.show()
                uploadImage(imageUrl!!)
            } else {

                Toast.makeText(applicationContext, "Fill your name or Press back.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val database = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser

        database.child("User").child(currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {

                        try {
                            // Data exists for the current user, you can retrieve it here
                            val user =
                                snapshot.getValue(UserData::class.java) // Assumes you have a User data class
                            // Do something with the retrieved user data
                            if (user != null) {
                                // Example: Display the user's name in a TextView
                                binding.profileName.text = user.userName.toString()
                                Picasso.get()
                                    .load(user.userImage)
                                    .placeholder(R.drawable.images) // Optional placeholder image
//                                .error(R.drawable.error) // Optional error image
                                    .fit()
                                    .centerCrop()
                                    .into(binding.profileImage)
                            }
                        } catch (e: java.lang.Exception) {

                            Toast.makeText(this@NavHeaderActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                            binding.profileName.text = e.message.toString()
                        }
                    } else {
                        // Data does not exist for the current user
                        // Handle this case
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    // Handle this case
                }
            })
    }


    private fun uploadImage(uri: Uri) {

        dialog.show()

//        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("User Profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        refStorage.putFile(uri)
            .addOnSuccessListener {

                it.storage.downloadUrl.addOnSuccessListener { image ->

                    val imageUrl = image.toString()
                    val userData = UserData(imageUrl, binding.tvProfileName.text.toString())
                    database.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(userData)
                    dialog.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener {

                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT)
                    .show()
            }

    }


    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode == Activity.RESULT_OK) {
            imageUrl = it.data!!.data
            binding.profileImage.setImageURI(imageUrl)
        }
    }

}