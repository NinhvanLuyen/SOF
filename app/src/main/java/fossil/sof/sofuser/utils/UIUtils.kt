package fossil.sof.sofuser.utils

import android.graphics.Typeface
import fossil.sof.sofuser.App
import fossil.sof.sofuser.application.LanguageID
import fossil.sof.sofuser.application.PreferencesKeys

/**
 * Created by ninhvanluyen on 16/11/18.
 */
object UIUtils {
    /**
     * Get selected language of this app
     *
     * @return language
     */
    fun getLanguage(): String {
        return App.app.sharedPreferences.getString(PreferencesKeys.LANGUAGE_ID, LanguageID.ENG)
    }

    /**
     * Set selected language of this app
     *
     * @param value language
     */
    fun setLanguage(value: String) {
        App.app.sharedPreferences.edit().putString(PreferencesKeys.LANGUAGE_ID, value).apply()
    }

    /**
     * Get font TypeFace
     *
     * @return
     */
    fun getTypeFace(): Typeface {
        return Typeface.createFromAsset(App.app.applicationContext.assets, "fonts/CaviarDreams.ttf")
    }
}