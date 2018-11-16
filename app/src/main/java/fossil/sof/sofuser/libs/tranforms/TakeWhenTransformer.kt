package fossil.sof.sofuser.libs.tranforms

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

/**
 * Created by ninhvanluyen on 1/11/18.
 */
class TakeWhenTransformer<S, T>(private val observable: Observable<T>) : ObservableTransformer<S, S> {

    override fun apply(upstream: Observable<S>): ObservableSource<S> {
        return observable.withLatestFrom(upstream, BiFunction { _, x -> x })
    }

}