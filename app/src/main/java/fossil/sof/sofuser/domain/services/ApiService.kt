package fossil.sof.sofuser.domain.services

import fossil.sof.sofuser.data.api.responses.LoadMoreData
import fossil.sof.sofuser.domain.models.Reputation
import fossil.sof.sofuser.domain.models.User
import io.reactivex.Single

/**
 * Created by ninhvanluyen on 16/11/18.
 */
interface ApiService {
    fun getListUser(page: Int): Single<LoadMoreData<out User>>
    fun getReputation(page: Int,userId:String): Single<LoadMoreData<out Reputation>>
}