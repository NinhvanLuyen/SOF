package fossil.sof.sofuser.libs

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fossil.sof.sofuser.libs.qualifers.RequireFragmentViewModel
import fossil.sof.sofuser.application.Constants
import fossil.sof.sofuser.utils.BundleUtils
import fossil.sof.sofuser.utils.ValidateUtils
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import fossil.sof.sofuser.R
import fossil.sof.sofuser.dialogs.DialogManager
import timber.log.Timber

/**
 * Created by ninhvanluyen on 16/11/18.
 */
open class BaseFragment<ViewModelType : FragmentViewModel> : Fragment(), LifecycleProvider<FragmentEvent>, FragmentLifecycle {

    private val lifecycle: BehaviorSubject<FragmentEvent> = BehaviorSubject.create()
    protected var viewModel: ViewModelType? = null
    private val VIEW_MODEL_KEY = "FragmentViewModel"
    private var mProgressDialog: ProgressDialog? = null

    var title: String = Constants.EMPTY


    @NonNull
    @CheckResult
    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> =
            RxLifecycleAndroid.bindFragment(lifecycle)

    @NonNull
    @CheckResult
    override fun <T : Any?> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> =
            RxLifecycle.bindUntilEvent(lifecycle, event)

    @NonNull
    @CheckResult
    override fun lifecycle(): Observable<FragmentEvent> = lifecycle

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Timber.d("onAttach " + this.javaClass.name + " | isAdded: " + isAdded)
        lifecycle.onNext(FragmentEvent.ATTACH)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.onNext(FragmentEvent.CREATE)

        assignViewModel(savedInstanceState)
        viewModel?.arguments(arguments)
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView " + this.javaClass.name + " | isAdded: " + isAdded)
        val view = super.onCreateView(inflater, container, savedInstanceState)
        lifecycle.onNext(FragmentEvent.CREATE_VIEW)
        return view
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycle.onNext(FragmentEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        Timber.d("onResume " + this.javaClass.name + " | isAdded: " + isAdded)
        lifecycle.onNext(FragmentEvent.RESUME)

//        assignViewModel(null)
        viewModel?.onResume(this)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        lifecycle.onNext(FragmentEvent.PAUSE)
        viewModel?.onPause()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        lifecycle.onNext(FragmentEvent.STOP)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.onNext(FragmentEvent.DESTROY_VIEW)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        lifecycle.onNext(FragmentEvent.DESTROY)
        viewModel?.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        Timber.e("onDetach " + this.javaClass.name + " | isAdded: " + isAdded)
        if (activity!!.isFinishing) {
            lifecycle.onNext(FragmentEvent.DETACH)
            if (viewModel != null) {
                viewModel?.onDetach()
                FragmentViewModelManager.instance.destroy(viewModel!!)
                viewModel = null
                Timber.e("viewmodel is null")
            }
        }
    }

    /**
     * Sends activity result data to the view model.
     */
    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel?.activityResult(ActivityResult(requestCode, resultCode, data))
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val viewModelEnvelope = Bundle()
        Timber.e("viewmodel Assign if viewmoel ${(viewModel != null)}")
        if (viewModel != null)
            FragmentViewModelManager.instance.save(viewModel!!, viewModelEnvelope)
        outState.putBundle(VIEW_MODEL_KEY, viewModelEnvelope)
    }

    private fun assignViewModel(viewModelEnvelope: Bundle?) {
        if (viewModel == null) {
            val anotation = javaClass.kotlin.annotations.find { it is RequireFragmentViewModel } as RequireFragmentViewModel
            val a = FragmentViewModelManager.instance.fetch(context!!, anotation.value.java, BundleUtils.maybeGetBundle(viewModelEnvelope, VIEW_MODEL_KEY))
            viewModel = a as ViewModelType
        Timber.e("viewmodel Assign if viewmoel ${anotation.value.java}")
            Timber.e("viewmodel is assign $anotation")

        }
    }

    protected fun showErrorMessage(message: String) {
        if (!ValidateUtils.isEmpty(message)) {
            DialogManager.showDialogMessage(message, context!!)
            viewModel?.errorDialogShowed()
        }
    }

    protected fun showProgressLoading() {
        if (mProgressDialog == null || !mProgressDialog!!.isShowing) {
            mProgressDialog = ProgressDialog.show(activity, null, getString(R.string.please_wait))
        }
    }

    protected fun hideProgressLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }

    protected fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    protected fun showSnackBarMessage(message: String, view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun onBackPress() {
        Timber.e("onBackPress " + this.javaClass.name + " | isAdded: " + isAdded)
        if (isAdded && childFragmentManager.backStackEntryCount > 1)
            childFragmentManager.popBackStackImmediate()
    }
}