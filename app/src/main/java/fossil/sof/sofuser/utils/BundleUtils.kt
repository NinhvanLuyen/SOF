package fossil.sof.sofuser.utils

import android.os.Bundle

/**
 * Created by ninhvanluyen on 16/11/18.
 */
object BundleUtils {
    fun maybeGetBundle(state: Bundle?, key: String): Bundle? {
        if (state == null)
            return null
        return state.getBundle(key)
    }
}