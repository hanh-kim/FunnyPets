package com.hpk.funnypet.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photos(
    @Json(name = "id")
    val id: String?,
    @Json(name = "page")
    val page: Int?,
    @Json(name = "pages")
    val pages: Int?,
    @Json(name = "per_page")
    val perPage: Int?,
    @Json(name = "perpage")
    val perpage: Int?,
    @Json(name = "photo")
    val photo: List<Photo>?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "total")
    val total: Int?
)