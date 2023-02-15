package com.example.photo_search.data

import com.google.gson.annotations.SerializedName

data class PhotoInfoResponse(
    @SerializedName("photo") val photo: PhotoInfo?
)

data class PhotoInfo(
    @SerializedName("owner") val owner: Owner?,
    @SerializedName("dates") val dates: Dates?,
    @SerializedName("title") val title: Title?,
)

data class Owner(
    @SerializedName("realname") val realname: String?
)

data class Dates(
    @SerializedName("posted") val posted: String?
)

data class Title(
    @SerializedName("_content") val _content: String?
)