package com.hpk.funnypet.model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Description(
    @Json(name = "_content")
    val content: String?
) : Parcelable