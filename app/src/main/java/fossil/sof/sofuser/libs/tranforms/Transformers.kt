package fossil.sof.sofuser.libs.tranforms

import fossil.sof.sofuser.libs.apierror.ApiError
import io.reactivex.Observable
import io.reactivex.subjects.Subject

/**
 * Created by ninhvanluyen on 1/11/18.
 */
object Transformers {
    fun <T> observeForUI() = ObserveForUITransformer<T>()
    fun <S, T> takeWhen(observable: Observable<T>) = TakeWhenTransformer<S, T>(observable)
    fun <T> pipeApiErrorTo(errorSubject: Subject<ApiError>) = NeverApiErrorTransformer<T>(errorSubject::onNext)
    fun <T> neverApiError() = NeverApiErrorTransformer<T>()



}