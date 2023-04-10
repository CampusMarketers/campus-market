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
import com.example.sellingapp.model.ItemCategory
import com.example.sellingapp.model.UserItemData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class ItemUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemUploadBinding
    private lateinit var db2: DatabaseReference
    private lateinit var query: Query
    private var imageUrl: Uri? = null
    private lateinit var dialog: Dialog
    private lateinit var database: DatabaseReference
    private lateinit var itemCategory:ItemCategory

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

        binding.itemImage.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"


            launchGalleryActivity.launch(intent)
        }
        var imageUrl2: String = ""
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.saveButton.setOnClickListener {
//            if(imageUrl==null || binding.itemPrice==null || binding.itemName==null || binding.itemDescription==null || binding.txtCategory.text.toString().length==null) {
//                Toast.makeText(this, "Empty fields are not allowed.", Toast.LENGTH_SHORT).show()
//            }
            if (imageUrl != null && binding.itemName.text.toString() != "" && binding.itemPrice.text.toString() != "" && binding.itemDescription.text.toString() != "" && binding.txtCategory.text.toString() != "Select Item Category") {
                uploadImage(imageUrl!!)
                dialog.show()

                val fileName = UUID.randomUUID().toString()
                val refStorage = FirebaseStorage.getInstance().reference.child("Items")
                    .child(fileName)

                refStorage.putFile(imageUrl!!)
                    .addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener { image ->
                            imageUrl2 = image.toString()
                            try {
                                val db = FirebaseDatabase.getInstance().getReference("Items")
                                    .child(binding.txtCategory.text.toString())
                                    .child("childItem")
                                val childItemId = db.push().key
                                val itemData = UserItemData(
                                    imageUrl2,
                                    binding.itemName.text.toString(),
                                    currentUser!!.uid,
                                    childItemId,
                                    binding.itemDescription.text.toString(),
                                    System.currentTimeMillis(),
                                    binding.itemPrice.text.toString()
                                )
                                if (childItemId != null)
                                    db.child(childItemId).setValue(itemData)

                            } catch (e: java.lang.Exception) {
                                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                            }

                            // to upload data in user

                            try {
                                val data = FirebaseAuth.getInstance().currentUser
                                val db2 = FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .child("sellItem")
                                val childItemId = db2.push().key
                                val itemData = UserItemData(
                                    imageUrl2,
                                    binding.itemName.text.toString(),
                                    currentUser!!.uid,
                                    childItemId,
                                    binding.itemDescription.text.toString(),
                                    System.currentTimeMillis(),
                                    binding.itemPrice.text.toString()
                                )
                                if (childItemId != null)
                                    db2.child(childItemId).setValue(itemData)

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
                        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast.makeText(this, "Empty fields are not allowed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //<----------------------FUNCTION TO UPLOAD IMAGE IN THE STORAGE OF DATABASE---------------------------->///
    private fun uploadImage(uri: Uri) {
        dialog.show()

        val fileName = UUID.randomUUID().toString()
        val refStorage = FirebaseStorage.getInstance().reference.child("Items")
            .child(fileName)

        refStorage.putFile(uri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                  //  imageUrl2 = image.toString()

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
            binding.itemImage.setImageURI(imageUrl)
        }
    }

}