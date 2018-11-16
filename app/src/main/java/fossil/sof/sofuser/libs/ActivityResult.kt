package fossil.sof.sofuser.libs

import android.app.Activity
import android.content.Intent

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class ActivityResult(val requestCode: Int, val resultCode: Int, val intent: Intent?) {
    fun isCanceled(): Boolean {
        return resultCode == Activity.RESULT_CANCELED
    }

    fun isOk(): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}

class ActivityResultPermission(val requestCode: Int,val permissions: Array<out String>,val grantResults: IntArray) {

}