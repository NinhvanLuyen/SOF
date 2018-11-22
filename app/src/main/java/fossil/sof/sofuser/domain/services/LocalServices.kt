package fossil.sof.sofuser.domain.services

import fossil.sof.sofuser.data.entities.UserEntity
import io.reactivex.Single

/**
 * Created by ninhvanluyen on 16/11/18.
 */
interface LocalServices {
    fun addBookmark(user: UserEntity): Single<Boolean>
    fun getUserBookmarked(): Single<MutableList<UserEntity>>
}
