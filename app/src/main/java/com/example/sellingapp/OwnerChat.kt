package com.example.sellingapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellingapp.Adapters.ChatAdapter
import com.example.sellingapp.databinding.ActivityOwnerChatBinding
import com.example.sellingapp.model.ItemCategory
import com.example.sellingapp.model.UserData
import com.example.sellingapp.model.UserItemData
import com.example.sellingapp.model.messageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OwnerChat : AppCompatActivity() {
    private lateinit var binding: ActivityOwnerChatBinding
    private lateinit var adapter: ChatAdapter
    private var messages: ArrayList<messageModel>? = null
    private var senderRoom: String? = null
    private var receiverRoom: String? = null
    private var database: FirebaseDatabase? = null
    private var storage: FirebaseStorage? = null
    private var dialog: ProgressDialog? = null
    private var senderUid: String? = null
    private var receiverUid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        dialog = ProgressDialog(this@OwnerChat)
        dialog!!.setMessage("Uploading image...")
        dialog!!.setCancelable(false)
        messages = ArrayList()

        val item = intent.getParcelableExtra<ItemCategory>("item")


        if (item != null) {
            binding.ownerName.text = item.category.toString()
            Picasso.get()
                .load(item.catPicUrl)
                .placeholder(R.drawable.images) // Optional placeholder image
//                                .error(R.drawable.error) // Optional error image
                .fit()
                .centerCrop()
                .into(binding.ownerProfileImage)

            receiverUid = item.catUid.toString()
            senderUid = FirebaseAuth.getInstance().uid

            senderRoom = senderUid + receiverUid
            receiverRoom = receiverUid + senderUid

            database!!.reference.child("Presence").child(receiverUid!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val status = snapshot.getValue(String::class.java)
                            if (status == "offline") {
                                binding.online.visibility = View.GONE
                            } else {
                                binding!!.online.setText(status)
                                binding!!.online.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


            adapter = ChatAdapter(this@OwnerChat, messages, senderRoom!!, receiverRoom!!)
            binding!!.messageRecyclerView.layoutManager = LinearLayoutManager(this@OwnerChat)
            binding!!.messageRecyclerView.adapter = adapter
            database!!.reference.child("chats")
                .child(senderRoom!!)
                .child("message")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messages!!.clear()
                        for (snapshot1 in snapshot.children) {
                            val message: messageModel? =
                                snapshot1.getValue(messageModel::class.java)
                            message!!.messageId = snapshot1.key
                            messages!!.add(message)
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


            val handler = Handler()
            binding!!
                .messagebox.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        database!!.reference.child("Presence")
                            .child(senderUid!!)
                            .setValue("typing...")
                        handler.removeCallbacksAndMessages(null)
                        handler.postDelayed(userStoppedTyping, 1000)
                    }

                    var userStoppedTyping = Runnable {
                        database!!.reference.child("Presence")
                            .child(senderUid!!)
                            .setValue("Online")
                    }


                })

            supportActionBar?.setDisplayShowTitleEnabled(false)

            binding!!.sendBtn.setOnClickListener {
                val messageTxt: String = binding.messagebox.text.toString()
                val date = Date()
                val message1 = messageModel(messageTxt, senderUid, date.time)

                binding.messagebox.setText("")
                val randomKey = database!!.reference.push().key
                val lastMsgObj = HashMap<String, Any>()
                lastMsgObj["lastMsg"] = message1.message!!
                lastMsgObj["lastMsgTime"] = date.time

                database!!.reference.child("chats").child(senderRoom!!)
                    .updateChildren(lastMsgObj)
                database!!.reference.child("chats").child(receiverRoom!!)
                    .updateChildren(lastMsgObj)
                database!!.reference.child("chats").child(senderRoom!!)
                    .child("message")
                    .child(randomKey!!)
                    .setValue(message1).addOnSuccessListener {
                        database!!.reference.child("chats")
                            .child(receiverRoom!!)
                            .child("message")
                            .child(randomKey)
                            .setValue(message1)
                            .addOnSuccessListener {

                            }
                    }


            }

            binding.attachment.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent!!.type = "image/*"
                startActivityForResult(intent, 25)
            }

            // start code here on click adapter item
            adapter.onItemClick = {
                val intent = Intent(this, FullScreenImage::class.java)
                intent.putExtra("item", it.imageUrl)
                startActivity(intent)
            }
            binding.ownerProfileImage.setOnClickListener {
                val intent = Intent(this, FullScreenImage::class.java)
                intent.putExtra("item", item.catPicUrl)
                startActivity(intent)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 25) {
            if (data != null) {
                if (data.data != null) {
                    val selectedImage = data.data
                    val calender = Calendar.getInstance()
                    var reference = storage!!.reference.child("chats")
                        .child(calender.timeInMillis.toString() + "")
                    dialog!!.show()
                    reference.putFile(selectedImage!!)
                        .addOnCompleteListener {
                            dialog!!.dismiss()
                            if (it.isSuccessful) {
                                reference.downloadUrl.addOnSuccessListener {
                                    val filePath = it.toString()
                                    val messageTxt: String = binding.messagebox.text.toString()
                                    val date = Date()
                                    val message2 = messageModel(messageTxt, senderUid, date.time)
                                    message2.message = "photo"
                                    message2.imageUrl = filePath
                                    binding.messagebox.setText("")

                                    val randomKey = database!!.reference.push().key

                                    val lastMesObj = HashMap<String, Any>()
                                    lastMesObj["lastMsg"] = message2.message!!
                                    lastMesObj["lastMsgTime"] = date.time
                                    database!!.reference.child("chats").updateChildren(lastMesObj)
                                    database!!.reference.child("chats").child(receiverRoom!!)
                                        .updateChildren(lastMesObj)
                                    database!!.reference.child("chats").child(senderRoom!!)
                                        .child("message").child(randomKey!!)
                                        .setValue(message2).addOnSuccessListener {
                                            database!!.reference.child("chats")
                                                .child(receiverRoom!!).child("message")
                                                .child(randomKey)
                                                .setValue(message2)
                                                .addOnSuccessListener {

                                                }
                                        }


                                }
                            }
                        }

                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence").child(currentId!!)
            .setValue("offline")

    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence").child(currentId!!)
            .setValue("online")
    }
}