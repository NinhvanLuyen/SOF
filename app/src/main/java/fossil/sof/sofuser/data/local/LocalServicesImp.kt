package fossil.sof.sofuser.data.local

import android.content.SharedPreferences
import fossil.sof.sofuser.domain.services.LocalServices
import fossil.sof.sofuser.application.Constants
import fossil.sof.sofuser.application.PreferencesKeys
import io.reactivex.Single


/**
 * Created by ninhvanluyen on 16/11/18.
 */
class LocalServicesImp(private val sharedPreferences: SharedPreferences) : LocalServices {

    override fun getUserToken(): Single<String> {
        return Single.create { t ->
            run {
                val data = sharedPreferences.getString(PreferencesKeys.USER_TOKEN, Constants.EMPTY)
                t.onSuccess(data)
            }
        }
    }

    override fun saveUserToken(token: String): Single<Boolean> {
        return Single.create { t ->
            run {
                sharedPreferences.edit()
                        .putString(PreferencesKeys.USER_TOKEN, token).apply()
                t.onSuccess(true)
            }
        }
    }

}
