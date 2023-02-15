package com.example.photo_search.data

import com.example.photo_search.API_RESULT_URL
import com.example.photo_search.API_GET_INFO
import com.example.photo_search.API_KEY
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

data class PhotosResponse(
    @SerializedName("photos") val photos: Photos?
)

data class Photos(
    @SerializedName("photo") val photo: List<Photo>?
)

data class Photo(
    @SerializedName("id") val id: String?,
    @SerializedName("secret") val secret: String?,
    @SerializedName("server") val server: String?,
    var photoInfo: PhotoInfo?,
) {
    suspend fun setAdditionalData(flickrApi: FlickrApi) {
        withContext(Dispatchers.IO) {
            val photoInfoCall = flickrApi.getPhotoInfo(
                API_GET_INFO,
                API_KEY,
                id
            ).execute()
            photoInfo = photoInfoCall.body()?.photo
        }
    }

    fun getUrl(): String {
        return "${API_RESULT_URL}/$server/${id}_$secret.jpg"
    }

    fun getAuthorAndDate(): String {
        val date = photoInfo?.dates?.posted?.toLong()?.let { Date(it * 1000) }
        val sdf: SimpleDateFormat = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val formattedDate = sdf.format(date)


        return photoInfo?.owner?.realname + " / " + formattedDate
    }

    fun getCustomTitle(): String {
        val title_ = photoInfo?.title?._content
        return if (title_ == null) {
            ""
        } else if (title_.length > 35) {
            title_.substring(0, Integer.min(title_.length, 35)) + "..."

        } else {
            title_
        }
    }
}