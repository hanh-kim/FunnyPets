package com.hpk.funnypet.api

import retrofit2.Call
import retrofit2.http.GET

interface MainInterface {
    companion object {
        private const val MAIN = "/services/rest/?method=flickr.favorites.getPublicList&api_key=87795ef21dc929b358157a5b2472701d&user_id=198730384%40N08&extras=description%2C+license%2C+date_upload%2C+date_taken%2C+icon_server%2C+original_format%2C+last_update%2C+geo%2C+tags%2C+machine_tags%2C+o_dims%2C+views%2C+media%2C+path_alias%2C+url_sq%2C+url_t%2C+url_s%2C+url_q%2C+url_m%2C+url_n%2C+url_z%2C+url_c%2C+url_l%2C+url_o&per_page=10&format=json&nojsoncallback=1"
    }
    @GET(MAIN)
    fun getMain(): Call<Any>
}