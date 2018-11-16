package fossil.sof.sofuser.libs

import android.content.Intent
import fossil.sof.sofuser.libs.apierror.ApiError
import fossil.sof.sofuser.application.Constants
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by ninhvanluyen on 16/11/18.
 */
open class ActivityViewModel{

    private val viewChange: PublishSubject<fossil.sof.sofuser.libs.ActivityLifecycle> = PublishSubject.create()
    private val view: Observable<fossil.sof.sofuser.libs.ActivityLifecycle> = viewChange.filter { view -> view != noView }
    private val activityResult: PublishSubject<ActivityResult> = PublishSubject.create()
    private val activityResultPermission: PublishSubject<ActivityResultPermission> = PublishSubject.create()
    private val intent: PublishSubject<Intent> = PublishSubject.create()

    private val noView: fossil.sof.sofuser.libs.ActivityLifecycle = ActivityLifecycle()

    private class ActivityLifecycle : fossil.sof.sofuser.libs.ActivityLifecycle {
        override fun lifecycle(): Observable<ActivityEvent> {
            return Completable.complete().toObservable()
        }
    }


    fun activityResult(activityResult: ActivityResult) {
        this.activityResult.onNext(activityResult)
    }
    fun activityResultPermision(activityResult: ActivityResultPermission) {
        this.activityResultPermission.onNext(activityResult)
    }

    fun intent(intent: Intent) {
        this.intent.onNext(intent)
    }

    fun onCreate() {
        dropView()
    }

    fun onResume(view: fossil.sof.sofuser.libs.ActivityLifecycle) {
        onTakeView(view)
    }

    fun onPause() {
        dropView()
    }

    fun onDestroy() {
        viewChange.onComplete()
    }

    private fun dropView() {
        viewChange.onNext(noView)
    }

    private fun onTakeView(view: fossil.sof.sofuser.libs.ActivityLifecycle) {
        viewChange.onNext(view)
    }

    protected fun activityResult(): Observable<ActivityResult> {
        return activityResult
    }
    protected fun activityResultPermission():Observable<ActivityResultPermission> =activityResultPermission

    protected fun intent(): Observable<Intent> {
        return intent
    }

    fun <T> bindTolifecycle(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.takeUntil<T> {
                view.switchMap { v -> v.lifecycle().map { e -> Pair(v, e) } }
                        .filter { ve -> isFinished(ve.first, ve.second) }
            }
        }
    }

    private fun isFinished(view: fossil.sof.sofuser.libs.ActivityLifecycle, event: ActivityEvent): Boolean {
        if (view is BaseActivity<*>) {
            return event == ActivityEvent.DESTROY && view.isFinishing
        }
        return event == ActivityEvent.DESTROY
    }

    protected var apiError = BehaviorSubject.create<ApiError>()
    fun errorDialogShowed() {
        apiError.onNext(ApiError(0, Constants.EMPTY))
    }

}