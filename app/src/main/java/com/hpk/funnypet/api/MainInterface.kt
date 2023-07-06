package com.hpk.funnypet.api

import com.hpk.funnypet.model.PhotosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MainInterface {
    companion object {
        private const val API_KEY="c80727edc0b98577bf0989a24613ad08"
        private const val USER_ID="191864893%40N06"
        private const val FAVORITE_PHOTOS = "/services/rest/?method=flickr.favorites.getPublicList&api_key=${API_KEY}&user_id=${USER_ID}&extras=description%2C+license%2C+date_upload%2C+date_taken%2C+icon_server%2C+original_format%2C+last_update%2C+geo%2C+tags%2C+machine_tags%2C+o_dims%2C+views%2C+media%2C+path_alias%2C+url_sq%2C+url_t%2C+url_s%2C+url_q%2C+url_m%2C+url_n%2C+url_z%2C+url_c%2C+url_l%2C+url_o&per_page=10&format=json&nojsoncallback=1"
    }
    @GET(FAVORITE_PHOTOS)
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page")limit: Int
    ): Call<PhotosResponse>
}