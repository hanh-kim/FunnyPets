package com.hpk.funnypet.model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Photo(
    @Json(name = "dateupload")
    val dateupload: String?,
    @Json(name = "description")
    val description: Description?,
    @Json(name = "height_c")
    val heightC: Int?,
    @Json(name = "height_l")
    val heightL: Int?,
    @Json(name = "height_m")
    val heightM: Int?,
    @Json(name = "height_n")
    val heightN: Int?,
    @Json(name = "height_o")
    val heightO: Int?,
    @Json(name = "height_q")
    val heightQ: Int?,
    @Json(name = "height_s")
    val heightS: Int?,
    @Json(name = "height_sq")
    val heightSq: Int?,
    @Json(name = "height_t")
    val heightT: Int?,
    @Json(name = "height_z")
    val heightZ: Int?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "tags")
    val tags: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "url_c")
    val urlC: String?,
    @Json(name = "url_l")
    val urlL: String?,
    @Json(name = "url_m")
    val urlM: String?,
    @Json(name = "url_n")
    val urlN: String?,
    @Json(name = "url_o")
    val urlO: String?,
    @Json(name = "url_q")
    val urlQ: String?,
    @Json(name = "url_s")
    val urlS: String?,
    @Json(name = "url_sq")
    val urlSq: String?,
    @Json(name = "url_t")
    val urlT: String?,
    @Json(name = "url_z")
    val urlZ: String?,
    @Json(name = "views")
    val views: String?,
    @Json(name = "width_c")
    val widthC: Int?,
    @Json(name = "width_l")
    val widthL: Int?,
    @Json(name = "width_m")
    val widthM: Int?,
    @Json(name = "width_n")
    val widthN: Int?,
    @Json(name = "width_o")
    val widthO: Int?,
    @Json(name = "width_q")
    val widthQ: Int?,
    @Json(name = "width_s")
    val widthS: Int?,
    @Json(name = "width_sq")
    val widthSq: Int?,
    @Json(name = "width_t")
    val widthT: Int?,
    @Json(name = "width_z")
    val widthZ: Int?
) : Parcelable {
    fun getUrl(): String? {
        val listUrls = listOf(urlSq, urlT, urlQ, urlS, urlN, urlM, urlZ, urlC, urlL, urlO).mapNotNull { it }
                .filter { it.isNotEmpty() }
        return listUrls.lastOrNull()
    }

    fun getViewCount(): String {
        val times = 155
        val viewCount = views?.toLongOrNull()
        return if (viewCount == null || viewCount == 0L) times.toString()  else {
            viewCount.times(times).toString()
        }
    }
}