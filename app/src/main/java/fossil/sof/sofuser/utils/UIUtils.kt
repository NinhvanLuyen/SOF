package fossil.sof.sofuser.utils

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import fossil.sof.sofuser.App
import fossil.sof.sofuser.R
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
    fun getTypeFace(contex:Context): Typeface {
        return ResourcesCompat.getFont(contex,R.font.caviardreams)!!
    }
    fun getShortNumberFormat(number: Int): String {
        return when {
            number >= 1000000000 -> when {
                number % 1000000000 >= 100000000 -> "${number / 1000000000}.${number % 1000000000 / 10000000}B"
                number % 1000000000 >= 10000000 -> "${number / 1000000000}.0${number % 1000000000 / 10000000}B"
                else -> "${number / 1000000000}B"
            }
            number >= 1000000 -> when {
                number % 1000000 >= 100000 -> "${number / 1000000}.${number % 1000000 / 10000}M"
                number % 1000000 >= 10000 -> "${number / 1000000}.0${number % 1000000 / 10000}M"
                else -> "${number / 1000000}M"
            }
            number >= 10000 -> "${number / 1000}K"
            else -> getNumberFormat(number)
        }
    }
    fun getNumberFormat(num: Int): String {
        var number = num
        var negative = false
        if (number < 0) {
            negative = true
            number = number * -1
        }
        val strNum = number.toString()
        if (strNum.length <= 3) {
            return if (negative)
                "-$strNum"
            else
                strNum
        } else {
            var result = ""
            val loop = strNum.length / 3
            var start = strNum.length % 3
            for (i in 0..loop) {
                if (i == 0 || result.isEmpty()) {
                    if (start > 0) {
                        result = strNum.substring(0, start)
                    } else {
                        start += 3
                    }
                } else {
                    result += "," + strNum.substring(start, start + 3)
                    start += 3
                }
            }
            return if (negative)
                "-$result"
            else
                result
        }
    }
}