package fossil.sof.sofuser.libs

import android.os.Bundle
import android.support.annotation.CallSuper
import fossil.sof.sofuser.libs.apierror.ApiError
import fossil.sof.sofuser.application.Constants
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 * Created by ninhvanluyen on 16/11/18.
 */
open class FragmentViewModel() {
    private val viewChange: PublishSubject<FragmentLifecycle> = PublishSubject.create()
    private val view: Observable<FragmentLifecycle> = viewChange.filter { view -> view != noView }
    private val noView: FragmentLifecycle = FragmentLifecycleNOView()
    private val arguments: PublishSubject<Bundle> = PublishSubject.create()
    private val activityResult: PublishSubject<ActivityResult> = PublishSubject.create()

    private class FragmentLifecycleNOView : FragmentLifecycle {
        override fun lifecycle(): Observable<FragmentEvent> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    constructor(environment: Environment) : this() {

    }

    @CallSuper
    fun onCreate() {
        dropView()
    }

    fun arguments(bundle: Bundle?) {
        if (bundle != null)
            arguments.onNext(bundle)
        else
            arguments.onNext(Bundle())
    }

    protected fun arguments(): Observable<Bundle> {
        return arguments
    }

    fun activityResult(activityResult: ActivityResult) {
        this.activityResult.onNext(activityResult)
    }

    @CallSuper
    fun onResume(view: FragmentLifecycle) {
        onTakeView(view)
    }


    @CallSuper
    fun onPause() {
        dropView()
    }

    @CallSuper
    fun onDestroy() {
        dropView()
        Timber.e("dropView Favorite")

    }

    @CallSuper
    fun onDetach() {
        viewChange.onComplete()
    }

    private fun onTakeView(view: FragmentLifecycle) {
        viewChange.onNext(view)
    }

    protected fun activityResult(): Observable<ActivityResult> {
        return activityResult
    }

    private fun dropView() {
        viewChange.onNext(noView)
    }

    protected fun view(): Observable<FragmentLifecycle> {
        return view
    }

    fun <T> bindToLifecycle(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.takeUntil<T> {
                view.switchMap { v -> v.lifecycle() }
                        .filter { ve -> ve == FragmentEvent.DETACH }
            }
        }
    }

    protected var apiError = BehaviorSubject.create<ApiError>()
    fun errorDialogShowed() {
        apiError.onNext(ApiError(0, Constants.EMPTY))
    }
}