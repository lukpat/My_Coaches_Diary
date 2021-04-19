package cz.lpatak.mycoachesdiary.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import cz.lpatak.mycoachesdiary.R

class PreferenceManger(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.pref_file_app),
            Context.MODE_PRIVATE
        )
    }

    fun putStringValue(key: String, value: String) =
        sharedPreferences.edit { putString(key, value) }

    fun getStringValue(key: String) = with(sharedPreferences) { getString(key, null) }

    fun clearAllPreferences() {
        sharedPreferences.edit().clear().apply()
    }

}