package fossil.sof.sofuser.libs.tranforms

import fossil.sof.sofuser.data.api.responses.LoadMoreData
import fossil.sof.sofuser.libs.apierror.ApiError
import fossil.sof.sofuser.libs.exceptions.ApiException
import fossil.sof.sofuser.application.ErrorCodes
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class ApiTransformer<T> : SingleTransformer<LoadMoreData<T>, LoadMoreData<T>> {
    override fun apply(upstream: Single<LoadMoreData<T>>): SingleSource<LoadMoreData<T>> {


        return upstream.flatMap { loadMoreUser: LoadMoreData<T> ->
            if (loadMoreUser.getDatas() !=null)
                Single.create<LoadMoreData<T>> { t ->
                    if (loadMoreUser.getDatas() != null)
                        t.onSuccess(loadMoreUser)
                    else {
                        t.onError(ApiException(ApiError(ErrorCodes.NO_DATA_RESULT, "No Data")))
                    }
                }
            else
                Single.create<LoadMoreData<T>> { t -> t.onError(ApiException(ApiError(loadMoreUser.getErrorID(), loadMoreUser.getErrors()))) }
        }
    }
}