package com.hpk.funnypet.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hpk.funnypet.AndroidApplication
import com.hpk.funnypet.model.Photo
import java.lang.reflect.Type

object PreferenceUtil {

    private val preference: SharedPreferences =
        AndroidApplication.mInstance.getSharedPreferences(
            "funny_pet_shared_preference",
            Context.MODE_PRIVATE
        )

    private val preferenceEditor = preference.edit()

    private const val PREF_KEY_PHOTO_HISTORY = "PREF_KEY_PHOTO_HISTORY"


    var photoHistory: List<Photo>
        get() {
            val json = preference.getString(PREF_KEY_PHOTO_HISTORY, null) ?: return listOf()
            val type: Type = object : TypeToken<List<Photo>>() {}.type
            return Gson().fromJson(json, type)
        }
        set(value) {
            preferenceEditor.putString(PREF_KEY_PHOTO_HISTORY, Gson().toJson(value)).apply()
        }
}