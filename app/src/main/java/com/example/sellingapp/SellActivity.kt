package com.example.sellingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sellingapp.Adapters.Grid2Adapter
import com.example.sellingapp.databinding.ActivitySellBinding
import com.example.sellingapp.model.UserItemData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SellActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var query: Query
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var adapter: Grid2Adapter
    private lateinit var binding: ActivitySellBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySellBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener {

            val intent=Intent(this,ItemUploadActivity::class.java)
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener {

//            val intent=Intent(this,MainActivity::class.java)
//            startActivity(intent)
            finish()
        }

        adapter = Grid2Adapter(listOf())
        binding.sellRecyclerView.adapter = adapter
        binding.sellRecyclerView.layoutManager = GridLayoutManager(this, 2)

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currentUser!!.uid).child("sellItem")
            query = databaseReference.orderByChild("itemTimeStamp")
            query.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val items = mutableListOf<UserItemData>()
                    for (data in snapshot.children) {

                        val item = data.getValue(UserItemData::class.java)
                        if (item != null) {

                            items.add(item)
                        }
                    }
                    adapter.items = items
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                    Log.e("Firebase", error.message)
                }
            })

            adapter.onItemClick = {

                val intent = Intent(this, FullScreenImage::class.java)
                intent.putExtra("item", it.downloadedImageUrl)
                startActivity(intent)
                finish()
            }
        } catch (e: java.lang.Exception) {

            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}