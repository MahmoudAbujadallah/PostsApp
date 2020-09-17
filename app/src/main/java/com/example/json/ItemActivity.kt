package com.example.json

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_item.*

class ItemActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val itemID = intent.getStringExtra("itemID")
        txtIDs.setText("id: $itemID")

        val itemTitle = intent.getStringExtra("itemTitle")
        txtTitles.text = itemTitle

        val imagePath: String? = intent.getStringExtra("itemUrl")
        if(imagePath?.isNotEmpty()!!){
            val bitmap = BitmapFactory.decodeFile(imagePath)
            images?.setImageBitmap(bitmap)
        }
    }

}
