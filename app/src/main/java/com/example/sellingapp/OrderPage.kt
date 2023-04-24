package com.example.sellingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sellingapp.databinding.ActivityOrderPageBinding
import com.example.sellingapp.model.ItemCategory
import com.example.sellingapp.model.UserData
import com.example.sellingapp.model.UserItemData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.*

class OrderPage : AppCompatActivity() {

    private lateinit var db1: DatabaseReference
    private lateinit var db2: DatabaseReference

    private lateinit var binding: ActivityOrderPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityOrderPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<UserItemData>("item")

        if (item != null) {
//         val nameTextView = findViewById<TextView>(R.id.tvName)
            //val descriptionTextView = findViewById<TextView>(R.id.tvDescription)
            binding.descriptionText.text = item.userDescription.toString().capitalize()
            binding.costView.text = item.itemPrice
            Picasso.get()
                .load(item.itemImageId)
                .placeholder(R.drawable.images)
                .fit()
                .centerCrop()
                .into(binding.itemImages)
            // var catName: String = item.category.toString().capitalize()
        }

        binding.chatWithOwner.setOnClickListener{
//            var intent=Intent(this,OwnerChat::class.java)
//            intent.putExtra("item",item)
//            startActivity(intent)
            db1= FirebaseDatabase.getInstance().reference
            if(item!=null) {
                if (item.userUid.toString() != FirebaseAuth.getInstance().currentUser!!.uid) {

                    try {

                        db1.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        // Data exists for the current user, you can retrieve it here
                                        val user =
                                            snapshot.getValue(UserData::class.java) // Assumes you have a User data class

                                        // Do something with the retrieved user data
                                        if (user != null) {
                                            val date=Date()
                                            db2 =
                                                FirebaseDatabase.getInstance().reference.child("User")
                                                    .child(item.userUid.toString()).child("Friends")
                                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)

                                            var friendUid2 =
                                                ItemCategory(FirebaseAuth.getInstance().currentUser!!.uid,
                                                    user.userName,
                                                    user.userImage,
                                                    date.time
                                                )
                                            db2.setValue(friendUid2)

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

                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                    try {
                        db1.child("User").child(item.userUid.toString())
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        // Data exists for the current user, you can retrieve it here
                                        val user =
                                            snapshot.getValue(UserData::class.java) // Assumes you have a User data class

                                        // Do something with the retrieved user data
                                        if (user != null) {

                                            db2 =
                                                FirebaseDatabase.getInstance().reference.child("User")
                                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                                    .child("Friends")
                                                    .child(item.userUid.toString())
                                            val date=Date()
                                            var friendUid = ItemCategory(item.userUid.toString(),
                                                user.userName.toString(),
                                                user.userImage.toString(),
                                             date.time)
                                            db2.setValue(friendUid)
                                            Toast.makeText(this@OrderPage,
                                                user.userName+" Added Successfully",
                                                Toast.LENGTH_SHORT).show()
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

                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this,"Don't make fool yourself",Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(this,"item is null error",Toast.LENGTH_SHORT).show()
            }

            val intent =Intent(this,Customers::class.java)
            startActivity(intent)
            finish()
        }

    }
}