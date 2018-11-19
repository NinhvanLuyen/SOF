package fossil.sof.sofuser.domain.services

import fossil.sof.sofuser.domain.models.User
import io.reactivex.Single

/**
 * Created by ninhvanluyen on 16/11/18.
 */
interface LocalServices {
    fun addBookmark(user: User): Single<Boolean>
    fun getUserBookmarked(): Single<MutableList<User>>
}
