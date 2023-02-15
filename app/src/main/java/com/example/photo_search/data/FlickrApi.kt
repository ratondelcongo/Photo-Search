package com.example.photo_search.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("?per_page=25&nojsoncallback=1&format=json")
    fun getRecentPhotos(
        @Query("method") method: String?,
        @Query("api_key") apiKey: String?,
    ): Call<PhotosResponse>


    @GET("?per_page=20&nojsoncallback=1&format=json")
    fun searchPhotos(
        @Query("method") method: String?,
        @Query("api_key") apiKey: String?,
        @Query("text") text: String?,
    ): Call<PhotosResponse>

    @GET("?nojsoncallback=1&format=json")
    fun getPhotoInfo(
        @Query("method") method: String?,
        @Query("api_key") apiKey: String?,
        @Query("photo_id") photoId: String?,
    ): Call<PhotoInfoResponse>
}