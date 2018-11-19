package fossil.sof.sofuser.data.api

import fossil.sof.sofuser.data.api.responses.LoadMoreData
import fossil.sof.sofuser.data.entities.ReputationEntity
import io.reactivex.Single
import fossil.sof.sofuser.data.entities.UserEntity
import retrofit2.http.*

/**
 * Created by ninhvanluyen on 16/11/18.
 */
interface HttpRequest {

    @GET("users")
    fun getListUsers(@QueryMap option: Map<String, String>): Single<LoadMoreData<UserEntity>>

    @GET("users/{id}/reputation-history?")
    fun getListReputation(@Path("id") userID: String, @QueryMap option: Map<String, String>): Single<LoadMoreData<ReputationEntity>>


//    @GET("search/multi")
//    fun search(@QueryMap option: Map<String, String>): Single<SearchRes>

}