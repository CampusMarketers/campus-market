package com.example.sellingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.example.sellingapp.databinding.ActivityFullScreenImageBinding
import com.squareup.picasso.Picasso

class FullScreenImage : AppCompatActivity() {
    private lateinit var binding:ActivityFullScreenImageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the image path or URL from the Intent extra
        val imagePath = intent.getStringExtra("item")
        Picasso.get()
            .load(imagePath)
            .placeholder(R.drawable.images)
            .fit()
            .centerCrop()
            .into(binding.fullPhoto)

        val layoutParams = binding.fullPhoto.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.fullPhoto.layoutParams = layoutParams

        // Set an OnClickListener on the ImageView to finish the activity and return to the previous screen
        binding.bckbtn.setOnClickListener {
            finish()
        }
    }



}