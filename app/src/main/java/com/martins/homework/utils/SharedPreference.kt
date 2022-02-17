package com.martins.homework.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreference (context: Context) {
    private val preferenceName = PIN_CODE
    private val sharedPref: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    /*Stored String Data*/
    fun saveString(key_name: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key_name, text)
        editor.apply()
    }

    fun getPreferenceString(key_name: String): String? {
        return sharedPref.getString(key_name, "0000")
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}