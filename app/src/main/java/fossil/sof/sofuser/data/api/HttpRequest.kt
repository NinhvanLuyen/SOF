package fossil.sof.sofuser.data.api

import fossil.sof.sofuser.data.api.responses.LoadMoreUsers
import io.reactivex.Single
import fossil.sof.sofuser.data.entities.UserEntity
import retrofit2.http.*

/**
 * Created by ninhvanluyen on 16/11/18.
 */
interface HttpRequest {

    @GET("users")
    fun getListUsers(@QueryMap option: Map<String, String>): Single<LoadMoreUsers<UserEntity>>


//    @GET("search/multi")
//    fun search(@QueryMap option: Map<String, String>): Single<SearchRes>

}