package fossil.sof.sofuser.libs.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject



/**
 * Created by ninhvanluyen on 3/1/18.
 */


class RxBus {

    private val bus = PublishSubject.create<Any>()

    fun send(o: Any) {
        bus.onNext(o)
    }

    fun toObservable(): Observable<Any> {
        return bus
    }

}