package fossil.sof.sofuser.libs

import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.Observable

/**
 * Created by ninhvanluyen on 16/11/18.
 */
interface FragmentLifecycle {
    fun lifecycle(): Observable<FragmentEvent>
}