package com.hpk.funnypet.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotosResponse(
    @Json(name = "photoset")
    val photos: Photos?,
    @Json(name = "stat")
    val stat: String?
)