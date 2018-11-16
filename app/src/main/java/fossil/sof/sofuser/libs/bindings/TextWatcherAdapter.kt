package fossil.sof.sofuser.libs.bindings

import android.databinding.Observable
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 * Created by ninhvanluyen on 2/1/18.
 */
class TextWatcherAdapter(private val field: ObservableField<String>) : TextWatcher {
    val value = ObservableField<String>()

    private var isInEditMode = false
    private var listener = PublishSubject.create<String>()

    init {

        field.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                if (isInEditMode) {
                    return
                }

                value.set(field.get())
            }
        })
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        //
    }

    override fun afterTextChanged(s: Editable) {
        if (field.get() == null || field.get() != s.toString()) {
            isInEditMode = true
            field.set(s.toString())
            isInEditMode = false
            listener.onNext(s.toString().trim())
        }
    }

    fun onChangeListener(): PublishSubject<String> = listener
}