package com.bangkit.aispresso.data.storage

import android.content.Context
import android.content.SharedPreferences

class PreferencesClass  (val context: Context){

    companion object {
        const val MEETING_PREF = "USER_PREF"
    }

    val sharedPref: SharedPreferences = context.getSharedPreferences(MEETING_PREF,0)

    fun setValue(key : String, value : String){
        val editor: SharedPreferences.Editor = sharedPref.edit() // digunakan untuk membuat perizinan mengedit data
        editor.putString(key, value)
        editor.apply()
    }

    fun getValue(key : String) : String? {
        return sharedPref.getString(key, "")
    }
}