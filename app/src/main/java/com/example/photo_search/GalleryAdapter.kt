package com.example.photo_search

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.photo_search.data.Photo
import java.util.concurrent.Executors

class GalleryAdapter(private val photoList: ArrayList<Photo>) :
    RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.images_rev_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.photoTitleTextView.text = photoList[position].getCustomTitle()
        holder.photoAuthorAndDateTextView.text = photoList[position].getAuthorAndDate()

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val `in` = java.net.URL(photoList[position].getUrl()).openStream()
            val image = BitmapFactory.decodeStream(`in`)
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                holder.photoPictureImageView.setImageBitmap(image)
            }
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoTitleTextView: TextView = itemView.findViewById(R.id.photo_title)
        val photoPictureImageView: ImageView = itemView.findViewById(R.id.photo_picture)
        val photoAuthorAndDateTextView: TextView = itemView.findViewById(R.id.photo_author_and_date)
    }
}