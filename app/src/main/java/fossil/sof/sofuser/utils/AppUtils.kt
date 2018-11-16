package fossil.sof.sofuser.utils

import android.app.Activity
import android.content.Context
import android.os.Environment
import fossil.sof.sofuser.App
import fossil.sof.sofuser.application.Constants
import fossil.sof.sofuser.application.PreferencesKeys
import android.util.DisplayMetrics
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File


/**
 * Created by ninhvanluyen on 16/11/18.
 */
object AppUtils {
    /**
     * Get facebook app id of this app
     *
     * @return
     */
    fun getFacebookAppID(): String {
        return App.app.sharedPreferences.getString(PreferencesKeys.FACEBOOK_APP_ID, Constants.EMPTY)
    }

    fun isFHD(context: Activity) {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        Timber.e("width_screen is $width")
        Constants.isFHD = width >= 1080
    }
}