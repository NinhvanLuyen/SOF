package fossil.sof.sofuser.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import fossil.sof.sofuser.domain.services.LocalServices
import fossil.sof.sofuser.application.Constants
import fossil.sof.sofuser.application.PreferencesKeys
import fossil.sof.sofuser.domain.models.User
import io.reactivex.Single


/**
 * Created by ninhvanluyen on 16/11/18.
 */
class LocalServicesImp(private val sharedPreferences: SharedPreferences, private val gson: Gson) : LocalServices {

    override fun getUserBookmarked(): Single<MutableList<User>> {
        return Single.create { t ->
            run {
                val data = sharedPreferences.getString(PreferencesKeys.USER_BOOKMARK, Constants.EMPTY)
                var ls = gson.fromJson(data, Array<User>::class.java)
                t.onSuccess(ls.toMutableList())
            }
        }
    }

    override fun addBookmark(user: User): Single<Boolean> {
        return Single.create { t ->
            run {
                val data = sharedPreferences.getString(PreferencesKeys.USER_BOOKMARK, Constants.EMPTY)
                var ls = arrayOf<User>()
                if (data.isNotEmpty())
                    ls = gson.fromJson(data, Array<User>::class.java)
                //check exist
                var exist = false
                var lsTerm = ls.toMutableList()
                var iterator = lsTerm.iterator()
                if (ls.isNotEmpty())
                    while (iterator.hasNext()) {
                        if (iterator.next().getUser_id() == user.getUser_id()) {
                            exist = true
                            iterator.remove()
                        }
                    }
                if (!exist) {
                    lsTerm.add(user)
                }
                var json = gson.toJson(lsTerm)
                sharedPreferences.edit()
                        .putString(PreferencesKeys.USER_BOOKMARK, json).apply()
                t.onSuccess(true)
            }
        }
    }

}
