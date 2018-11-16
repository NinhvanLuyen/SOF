package fossil.sof.sofuser.libs.tranforms

import fossil.sof.sofuser.data.api.responses.LoadMoreUsers
import fossil.sof.sofuser.libs.apierror.ApiError
import fossil.sof.sofuser.libs.exceptions.ApiException
import fossil.sof.sofuser.application.ErrorCodes
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class ApiTransformer<T> : SingleTransformer<LoadMoreUsers<T>, LoadMoreUsers<T>> {
    override fun apply(upstream: Single<LoadMoreUsers<T>>): SingleSource<LoadMoreUsers<T>> {


        return upstream.flatMap { loadMoreUser: LoadMoreUsers<T> ->
            if (loadMoreUser.getErrorID() != 0)
                Single.create<LoadMoreUsers<T>> { t ->
                    if (loadMoreUser.getUsers() != null)
                        t.onSuccess(loadMoreUser)
                    else {
                        t.onError(ApiException(ApiError(ErrorCodes.NO_DATA_RESULT, "No Data")))
                    }
                }
            else
                Single.create<LoadMoreUsers<T>> { t -> t.onError(ApiException(ApiError(loadMoreUser.getErrorID(), loadMoreUser.getErrors()))) }
        }
    }
}