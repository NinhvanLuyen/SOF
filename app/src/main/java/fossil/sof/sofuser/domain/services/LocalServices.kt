package fossil.sof.sofuser.domain.services

import io.reactivex.Single

/**
 * Created by ninhvanluyen on 16/11/18.
 */
interface LocalServices {
    fun saveUserToken(token: String): Single<Boolean>
    fun getUserToken(): Single<String>
}
