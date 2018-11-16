package fossil.sof.sofuser.libs.preferences

import android.content.SharedPreferences
import fossil.sof.sofuser.application.Constants

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class StringPreference(private val sharedPreferences: SharedPreferences, private val key: String) {

    private var defaultValue: String = Constants.EMPTY

    fun get(): String {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun isSet(): Boolean {
        return sharedPreferences.contains(key)
    }

    fun set(value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun delete() {
        sharedPreferences.edit().remove(key).apply()
    }
}