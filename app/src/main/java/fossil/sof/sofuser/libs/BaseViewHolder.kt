package fossil.sof.sofuser.libs

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import fossil.sof.sofuser.App
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 * Created by ninhvanluyen on 1/10/18.
 */
abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    val context: Context
    private val lifecycle: PublishSubject<ActivityEvent> = PublishSubject.create()

    init {
        view.setOnClickListener(this)
        context = view.context
    }

    abstract fun onBind()

    abstract fun bindData(data: Any, position: Int)

    fun lifecycleEvent(event: ActivityEvent) {
        lifecycle.onNext(event)
    }

    protected fun environment(): Environment {
        return (context.applicationContext as (App)).component.environment()
    }

}