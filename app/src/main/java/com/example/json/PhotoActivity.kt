package com.example.json

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.json.Adapter.PhotoAdapter
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.item_photo_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class PhotoActivity : AppCompatActivity() {
    private var photoList = ArrayList<PhotoDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        GlobalScope.launch(Dispatchers.IO) {

            photoList = httpGet("https://jsonplaceholder.typicode.com/photos")

            GlobalScope.launch(Dispatchers.Main) {
                rclPhoto?.adapter = PhotoAdapter(photoList)
            }
        }

        rclPhoto.layoutManager = GridLayoutManager(this, 1)

        rclPhoto.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            var gestureDetector = GestureDetector(this@PhotoActivity, object : GestureDetector.SimpleOnGestureListener(){
                override fun onSingleTapUp(e: MotionEvent?): Boolean {
                    return true
                }
            })
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null && gestureDetector.onTouchEvent(e)){
                    val position = rv.getChildAdapterPosition(child)

                    val itemID = photoList[position].getID()
                    val itemTitle = photoList[position].getTitle()
                    val itemUrl = photoList[position].getUrl()

                    val intent = Intent(this@PhotoActivity, ItemActivity::class.java)
                    intent.putExtra("itemID", itemID)
                    intent.putExtra("itemTitle", itemTitle)
                    intent.putExtra("itemUrl", itemUrl)

                    startActivity(intent)
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })
    }

    private fun httpGet(myURL: String?): ArrayList<PhotoDataModel> {
        System.setProperty("http.keepAlive", "false");
        val json = StringBuffer()

        val url: URL = URL(myURL)

        val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.setRequestProperty("User-Agent","Chrome/18.0.1025.166")
        httpURLConnection.connectTimeout = 30000
        httpURLConnection.readTimeout = 30000

        val responseCode: Int = httpURLConnection.responseCode

        if (responseCode == 200) {
            val inStream: InputStream = httpURLConnection.inputStream
            val isReader = InputStreamReader(inStream)
            val bReader = BufferedReader(isReader)
            var line = bReader.readLine()

            while (line != null) {
                json.append(line)
                line = bReader.readLine()
            }
        }

        val jsonArray = JSONArray(json.toString())
        for (i: Int in 0 until 24) {
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            val albumId = jsonObject.getString("albumId")
            val id = jsonObject.getString("id")
            val title = jsonObject.getString("title")
            val url = jsonObject.getString("url")
            val thumbnailUrl = jsonObject.getString("thumbnailUrl")

            val thumbnailUrlConnection = URL(thumbnailUrl).openConnection()
            thumbnailUrlConnection.setRequestProperty("User-Agent","Mozilla/5.0")
            val thumbnailImage = BitmapFactory.decodeStream(thumbnailUrlConnection.getInputStream())

            val photo = PhotoDataModel(albumId, id, title, url, thumbnailUrl,  thumbnailImage)
            photoList.add(photo)
        }

        return photoList
    }

}
