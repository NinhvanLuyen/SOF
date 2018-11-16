package fossil.sof.sofuser.domain.services

import fossil.sof.sofuser.data.api.responses.LoadMoreUsers
import fossil.sof.sofuser.domain.models.User
import io.reactivex.Single

/**
 * Created by ninhvanluyen on 16/11/18.
 */
interface ApiService {
    fun getListUser(page: Int): Single<LoadMoreUsers<out User>>
}