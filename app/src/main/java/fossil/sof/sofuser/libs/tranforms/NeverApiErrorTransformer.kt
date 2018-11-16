package fossil.sof.sofuser.libs.tranforms

import fossil.sof.sofuser.libs.apierror.ApiError
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * Created by ninhvanluyen on 1/12/18.
 */
class NeverApiErrorTransformer<T> : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.doOnError { t ->
            run {
                errorAction?.invoke(ApiError.fromThrowable(t))
            }
        }.onErrorResumeNext { _: Throwable -> Observable.empty() }
    }

    val errorAction: ((ApiError) -> Any)?

    constructor() {
        errorAction = null
    }

    constructor(errorAction: (ApiError) -> Any) {
        this.errorAction = errorAction
    }
}