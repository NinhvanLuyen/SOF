package fossil.sof.sofuser.libs.tranforms

import android.os.Looper
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by ninhvanluyen on 1/11/18.
 */
class ObserveForUITransformer<T> : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.flatMap {
            if (Looper.getMainLooper().thread == Thread.currentThread())
                Observable.just(it)
            else
                Observable.just(it).observeOn(AndroidSchedulers.mainThread())
        }
    }
}