package fossil.sof.sofuser.libs

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import fossil.sof.sofuser.App
import fossil.sof.sofuser.R
import fossil.sof.sofuser.di.ApplicationComponent
import fossil.sof.sofuser.libs.qualifers.RequireActivityViewModel
import fossil.sof.sofuser.utils.BundleUtils
import fossil.sof.sofuser.utils.UIUtils
import fossil.sof.sofuser.utils.ValidateUtils
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import fossil.sof.sofuser.dialogs.DialogManager
import java.util.*

/**
 * Created by ninhvanluyen on 16/11/18.
 */
open class BaseActivity<ViewModelType : ActivityViewModel> : AppCompatActivity(), LifecycleProvider<ActivityEvent>, ActivityLifecycle {

    private val back: PublishSubject<Boolean> = PublishSubject.create()
    private val lifecycle: BehaviorSubject<ActivityEvent> = BehaviorSubject.create()
    private val VIEW_MODEL_KEY = "viewModel"
    protected var viewModel: ViewModelType? = null
    private var mProgressDialog: ProgressDialog? = null

    @CheckResult
    @NonNull
    override fun lifecycle(): Observable<ActivityEvent> = lifecycle

    @CheckResult
    @NonNull
    override fun <T : Any?> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> =
            RxLifecycle.bindUntilEvent(lifecycle, event)

    @CheckResult
    @NonNull
    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> =
            RxLifecycleAndroid.bindActivity(lifecycle)

    /**
     * Sends activity result data to the view model.
     */
    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel?.activityResult(ActivityResult(requestCode, resultCode, data))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel?.activityResultPermision(ActivityResultPermission(requestCode, permissions, grantResults))


    }


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocalize(UIUtils.getLanguage())
        lifecycle.onNext(ActivityEvent.CREATE)
        assignViewModel(savedInstanceState)

        viewModel?.intent(intent)
    }

    /**
     * Called when an activity is set to `singleTop` and it is relaunched while at the top of the activity stack.
     */

    @CallSuper
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null)
            viewModel?.intent(intent)
    }


    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycle.onNext(ActivityEvent.START)
        back.compose(bindUntilEvent(ActivityEvent.STOP))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { goBack() }
    }


    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycle.onNext(ActivityEvent.RESUME)
        assignViewModel(null)
        viewModel?.onResume(this)
    }


    @CallSuper
    override fun onPause() {
        super.onPause()
        lifecycle.onNext(ActivityEvent.PAUSE)
        viewModel?.onPause()
    }


    @CallSuper
    override fun onStop() {
        super.onStop()
        lifecycle.onNext(ActivityEvent.STOP)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        lifecycle.onNext(ActivityEvent.DESTROY)
        if (isFinishing) {
            if (viewModel != null) {
                ActivityViewModelManager.instance.destroy(viewModel!!)
                viewModel = null
            }
        }
    }

    override fun onBackPressed() {
        back.onNext(true)
    }

    /**
     * Override in subclasses for custom exit transitions. First item in pair is the enter animation,
     * second item in pair is the exit animation.
     */
    private fun exitTransition(): Pair<Int, Int>? {
        return Pair(R.anim.fade_in_slide_in_left, R.anim.slide_out_right)
    }

    private fun enterTransition(): Pair<Int, Int>? {
        return Pair(R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }


    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val viewModelEnvelope = Bundle()
        if (viewModel != null) {
            ActivityViewModelManager.instance.save(viewModel!!, viewModelEnvelope)
        }
        outState.putBundle(VIEW_MODEL_KEY, viewModelEnvelope)
    }

    fun startActivityWithTransition(intent: Intent) {
        startActivity(intent)
        val enterTransitions = enterTransition()
        if (enterTransitions != null)
            overridePendingTransition(enterTransitions.first, enterTransitions.second)
    }

    protected fun startActivityForResultWithTransition(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
        val enterTransitions = enterTransition()
        if (enterTransitions != null)
            overridePendingTransition(enterTransitions.first, enterTransitions.second)
    }

    protected fun application(): App {
        return application as App
    }

    protected fun component(): ApplicationComponent {
        return application().component
    }

    protected fun environment(): Environment {
        return application().component.environment()
    }

    /**
     * Triggers a back press with an optional transition.
     */
    private fun goBack() {
        super.onBackPressed()
    }

    protected fun exitAnimation() {
        val exitTransitions = exitTransition()
        if (exitTransitions != null) {
            overridePendingTransition(exitTransitions.first, exitTransitions.second)
        }
    }

    private fun assignViewModel(viewModelEnvelope: Bundle?) {
        if (viewModel == null) {
            val anotation = javaClass.kotlin.annotations.find { it is RequireActivityViewModel } as RequireActivityViewModel
            val a = ActivityViewModelManager.instance.fetch(this, anotation.value.java, BundleUtils.maybeGetBundle(viewModelEnvelope, VIEW_MODEL_KEY))
            viewModel = a as ViewModelType
        }
    }

    protected open fun showErrorMessage(message: String) {
        if (!ValidateUtils.isEmpty(message)) {
            DialogManager.showDialogMessage(message, this)
            viewModel?.errorDialogShowed()
        }
    }

    protected open fun showQuestionDialog(message: String, action: Int, listener: DialogInterface.OnClickListener) {
        if (!ValidateUtils.isEmpty(message)) {
            DialogManager.showDialogMessageWithAction(message, action, this, listener)
            viewModel?.errorDialogShowed()
        }
    }

    protected fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showSnackBarMessage(message: String, view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showProgressLoading() {
        if (mProgressDialog == null || !mProgressDialog!!.isShowing) {
            mProgressDialog = ProgressDialog.show(this, null, getString(R.string.please_wait))
        }
    }

    private fun hideProgressLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }

    protected fun renderLoading(isLoading: Boolean) {
        if (isLoading)
            showProgressLoading()
        else
            hideProgressLoading()
    }

    protected fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setLocalize(local: String) {
        val locale = Locale(local)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}