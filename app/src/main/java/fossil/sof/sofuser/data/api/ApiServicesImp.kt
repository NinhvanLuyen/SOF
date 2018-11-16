package fossil.sof.sofuser.data.api

import fossil.sof.sofuser.application.ErrorCodes
import fossil.sof.sofuser.application.ErrorMessage
import fossil.sof.sofuser.domain.services.ApiService
import io.reactivex.Single
import fossil.sof.sofuser.data.api.responses.LoadMoreUsers
import fossil.sof.sofuser.domain.models.User
import fossil.sof.sofuser.libs.apierror.ApiError
import fossil.sof.sofuser.libs.exceptions.ApiException
import fossil.sof.sofuser.libs.tranforms.ApiTransformer
import fossil.sof.sofuser.utils.DeviceUtils
import io.reactivex.schedulers.Schedulers

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class ApiServicesImp(val httpRequest: HttpRequest) : ApiService {
    override fun getListUser(page: Int): Single<LoadMoreUsers<out User>> {
        return Single.defer {
            if (!DeviceUtils.isNetworkAvailable()) {
                Single.error(ApiException(ApiError(ErrorCodes.NET_WORK_PROBLEM, ErrorMessage.NET_WORK_PROBLEM)))
            } else {
                val option = HashMap<String, String>()
                option.put("per_page", "21")
                option.put("orientation", "portrait")
                httpRequest.getListUsers(option)
                        .compose(ApiTransformer())
                        .subscribeOn(Schedulers.io())
            }
        }
    }


}