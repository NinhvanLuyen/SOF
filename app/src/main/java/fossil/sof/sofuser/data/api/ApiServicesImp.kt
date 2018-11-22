package fossil.sof.sofuser.data.api

import fossil.sof.sofuser.application.ErrorCodes
import fossil.sof.sofuser.application.ErrorMessage
import fossil.sof.sofuser.domain.services.ApiService
import io.reactivex.Single
import fossil.sof.sofuser.data.api.responses.LoadMoreData
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.domain.models.Reputation
import fossil.sof.sofuser.libs.apierror.ApiError
import fossil.sof.sofuser.libs.exceptions.ApiException
import fossil.sof.sofuser.libs.tranforms.ApiTransformer
import fossil.sof.sofuser.utils.DeviceUtils
import io.reactivex.schedulers.Schedulers

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class ApiServicesImp(val httpRequest: HttpRequest) : ApiService {
    override fun getListUser(page: Int): Single<LoadMoreData<UserEntity>> {
        return Single.defer {
            if (!DeviceUtils.isNetworkAvailable()) {
                Single.error(ApiException(ApiError(ErrorCodes.NET_WORK_PROBLEM, ErrorMessage.NET_WORK_PROBLEM)))
            } else {
                val option = HashMap<String, String>()
                option.put("page", "$page")
                option.put("pagesize", "30")
                option.put("site", "stackoverflow")
                httpRequest.getListUsers(option)
                        .compose(ApiTransformer())
                        .subscribeOn(Schedulers.io())
            }
        }
    }

    override fun getReputation(page: Int,userID:String): Single<LoadMoreData<out Reputation>> {
        return Single.defer {
            if (!DeviceUtils.isNetworkAvailable()) {
                Single.error(ApiException(ApiError(ErrorCodes.NET_WORK_PROBLEM, ErrorMessage.NET_WORK_PROBLEM)))
            } else {
                val option = HashMap<String, String>()
                option.put("page", "$page")
                option.put("pagesize", "30")
                option.put("site", "stackoverflow")
                httpRequest.getListReputation(userID,option)
                        .compose(ApiTransformer())
                        .subscribeOn(Schedulers.io())
            }
        }
    }


}