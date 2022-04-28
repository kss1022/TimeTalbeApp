package net.suwon.plus.data.preference

import android.content.SharedPreferences

class PreferenceManager(private val preferences: SharedPreferences) {


    companion object {
        const val PREFERENCES_NAME = "com.example.suwon_university_community"
        const val KEY_ID_TOKEN = "ID_TOKEN"
        const val KEY_VERIFIED = "VERIFIED"
        const val KEY_RECENTLY_LOGIN_ID = "RECENTLY_LOGIN_ID"

        const val KEY_TIME_TABLE_UPDATED_TIME = "TIME_TABLE_UPDATED"
        const val KEY_TIME_TABLE_MAIN = "TIME_TABLE_MAIN"

        const val KEY_NOTICE_UPDATED_TIME = "NOTICE_UPDATED"

        const val KEY_THEME_TYPE = "THEME_TYPE"

        private const val INVALID_STRING_VALUE = false
        private const val INVALID_BOOLEAN_VALUE = false
        private const val INVALID_LONG_VALUE = Long.MIN_VALUE
        private const val INVALID_INT_VALUE = Int.MIN_VALUE
    }

    private val editor by lazy { preferences.edit() }


    fun clear() {
        editor.clear()
        editor.apply()
    }

    /**
     * theme
     */

    fun putThemeType( type : Int){
        editor.putInt(KEY_THEME_TYPE, type)
        editor.apply()
    }

    fun getThemeType() : Int? {
        val type =  preferences.getInt(KEY_THEME_TYPE, INVALID_INT_VALUE)

        return if(type == INVALID_INT_VALUE){
            null
        }else{
            type
        }
    }



    /**
     * token check
     */
    fun putIdToken(idToken: String) {
        editor.putString(KEY_ID_TOKEN, idToken)
        editor.apply()
    }

    fun getIdToken(): String? = preferences.getString(KEY_ID_TOKEN, null)


    fun removeIdToken() {
        editor.putString(KEY_ID_TOKEN, null)
        editor.apply()
    }


    /**
     * verified saved
     */

    fun putVerified(isVerified: Boolean) {
        editor.putBoolean(KEY_VERIFIED, isVerified)
        editor.apply()
    }

    fun getVerified(): Boolean = preferences.getBoolean(KEY_VERIFIED, false)


    fun removeVerified() {
        editor.putBoolean(KEY_VERIFIED, false)
        editor.apply()
    }

    /**
     * recently login id
     */


    fun putRecentlyLoginId( id: String) {
        editor.putString(KEY_RECENTLY_LOGIN_ID, id)
        editor.apply()
    }

    fun getRecentlyLoginId(): String?{
        val value = preferences.getString(KEY_RECENTLY_LOGIN_ID, "")

        return if(value == ""){
            null
        }else{
            value
        }
    }



    /**
     * TimeTable updated time
     */

    fun putTimeTableUpdatedTime(updatedTime: Long) {
        editor.putLong(KEY_TIME_TABLE_UPDATED_TIME, updatedTime)
        editor.apply()
    }

    fun getTimeTableUpdatedTime(): Long? {
        val value = preferences.getLong(KEY_TIME_TABLE_UPDATED_TIME, INVALID_LONG_VALUE)

        return if (value == INVALID_LONG_VALUE) {
            null
        } else {
            value
        }
    }


    /**
     * Main TimeTable id
     */

    fun putMainTimeTableId(timeTableId: Long) {
        editor.putLong(KEY_TIME_TABLE_MAIN, timeTableId)
        editor.apply()
    }

    fun getMainTimeTableId(): Long?{
       val value =  preferences.getLong(KEY_TIME_TABLE_MAIN, INVALID_LONG_VALUE)

        return if(value == INVALID_LONG_VALUE){
            null
        }else{
            value
        }
    }


    /**
     * Notice
     */

    fun putNoticeUpdatedTime(updatedTime : Long){
        editor.putLong(KEY_NOTICE_UPDATED_TIME, updatedTime)
        editor.apply()
    }

    fun getNoticeUpdatedTime() : Long?{
        val value = preferences.getLong(KEY_NOTICE_UPDATED_TIME, INVALID_LONG_VALUE)

        return if(value == INVALID_LONG_VALUE){
             null
        }else{
            value
        }
    }
}