package com.example.json.Adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.json.PhotoDataModel
import com.example.json.R
import java.net.HttpURLConnection
import java.net.URL


class PhotoAdapter(var list:ArrayList<PhotoDataModel>): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtID.text = list[position].getID()
        holder.txtTitle.text = list[position].getTitle()
        holder.image?.setImageBitmap(list[position].getThumbnailImage())
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var image = view.findViewById<ImageView>(R.id.image)
        var txtID = view.findViewById<TextView>(R.id.txtID)
        var txtTitle = view.findViewById<TextView>(R.id.txtTitle)
    }
}