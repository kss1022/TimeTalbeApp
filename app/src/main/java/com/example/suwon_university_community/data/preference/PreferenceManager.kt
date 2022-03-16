package com.example.suwon_university_community.data.preference

import android.content.SharedPreferences

class PreferenceManager(private val preferences: SharedPreferences) {


    companion object{
        const val PREFERENCES_NAME = "com.example.suwon_university_community"
        const val KEY_ID_TOKEN = "ID_TOKEN"
        const val KEY_VERIFIED = "VERIFIED"
    }

    private val editor by lazy { preferences.edit() }


    fun clear() {
        editor.clear()
        editor.apply()
    }


    /**
     * token check
     */
    fun putIdToken( idToken : String){
        editor.putString(KEY_ID_TOKEN , idToken)
        editor.apply()
    }

    fun getIdToken() : String? =  preferences.getString(KEY_ID_TOKEN, null)


    fun removeIdToken() {
        editor.putString(KEY_ID_TOKEN, null)
        editor.apply()
    }


    /**
     * verified saved
     */

    fun putVerified( isVerified : Boolean ){
        editor.putBoolean(KEY_VERIFIED , isVerified)
        editor.apply()
    }

    fun getVerified() : Boolean = preferences.getBoolean(KEY_VERIFIED, false)
}