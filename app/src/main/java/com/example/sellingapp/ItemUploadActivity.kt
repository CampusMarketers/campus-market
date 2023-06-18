package com.example.sellingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sellingapp.databinding.ActivityItemUploadBinding
import com.example.sellingapp.model.UserItemData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ItemUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemUploadBinding
    private lateinit var dialog: Dialog
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var imageUrl: Uri? = null

    override fun onResume() {
        super.onResume()

        val category = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, category)
        binding.txtCategory.setAdapter(arrayAdapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityItemUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

//        binding.backButton.setOnClickListener {
//
//            val intent= Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        binding.itemImage.setOnClickListener {

            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }


        binding.saveButton.setOnClickListener {

            val category = binding.txtCategory.text.toString()
            val itemName = binding.itemName.text.toString().trim()
            val itemPrice = binding.itemPrice.text.toString().trim()
            val itemDescription = binding.itemDescription.text.toString().trim()
            if (imageUrl != null && category != "Select Item Category" && itemName.isNotEmpty() && itemPrice.isNotEmpty() && itemDescription.isNotEmpty()) {

//                uploadImage(imageUrl!!)
                dialog.show()

                val fileName = UUID.randomUUID().toString()
                val refStorage = FirebaseStorage.getInstance().reference.child("Items")
                    .child(fileName)

                refStorage.putFile(imageUrl!!)
                    .addOnSuccessListener {

                        it.storage.downloadUrl.addOnSuccessListener { image ->

                            val downloadedImageUrl = image.toString()

                            try {

                                val db = FirebaseDatabase.getInstance().getReference("Items")
                                    .child(category)
                                    .child("childItem")
                                val childItemId = db.push().key
                                val itemData = UserItemData(
                                    downloadedImageUrl,
                                    itemName,
                                    currentUser!!.uid,
                                    childItemId,
                                    itemDescription,
                                    System.currentTimeMillis(),
                                    itemPrice
                                )
                                db.child(childItemId!!).setValue(itemData)
                            } catch (e: java.lang.Exception) {

                                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                            }

                            try {

                                val db2 = FirebaseDatabase.getInstance().getReference("User")
                                    .child(currentUser!!.uid)
                                    .child("sellItem")
                                val childItemId = db2.push().key
                                val itemData = UserItemData(
                                    downloadedImageUrl,
                                    itemName,
                                    currentUser.uid,
                                    childItemId,
                                    itemDescription,
                                    System.currentTimeMillis(),
                                    itemPrice
                                )
                                db2.child(childItemId!!).setValue(itemData)
                            } catch (e: java.lang.Exception) {

                                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                            }

                            dialog.dismiss()
                            val intent = Intent(this, SellActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener {

                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
            } else {

                Toast.makeText(this, "Empty fields are not allowed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun uploadImage(uri: Uri) {
//
//        dialog.show()
//
//        val fileName = UUID.randomUUID().toString()
//        val refStorage = FirebaseStorage.getInstance().reference.child("Items")
//            .child(fileName)
//
//        refStorage.putFile(uri)
//            .addOnFailureListener {
//
//                dialog.dismiss()
//                Toast.makeText(this, "Network Problem.", Toast.LENGTH_SHORT)
//                    .show()
//            }
//    }

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode == Activity.RESULT_OK) {

            imageUrl = it.data!!.data
            binding.itemImage.setImageURI(imageUrl)
        }
    }
}