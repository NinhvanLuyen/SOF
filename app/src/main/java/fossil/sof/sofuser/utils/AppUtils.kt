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
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by ninhvanluyen on 16/11/18.
 */
object AppUtils {
    fun convertLongToStringDate(date: Long): String {
        var fm = SimpleDateFormat("dd-MM-yyyy")
        var calendar = Calendar.getInstance()
        calendar.time = Date(date)
        calendar.add(Calendar.SECOND, date.toInt())
        var date = calendar.time
        return fm.format(date)
    }

    fun getTime(): Long = GregorianCalendar.getInstance().time.time

}