package fossil.sof.sofuser.libs

import android.content.Context
import android.os.Bundle
import fossil.sof.sofuser.App
import java.util.*

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class ActivityViewModelManager {
    private val VIEW_MODEL_ID_KEY = "view_model_id"
    private val VIEW_MODEL_STATE_KEY = "view_model_state"
    private val viewModels = hashMapOf<String, ActivityViewModel>()

    companion object {
        val instance = ActivityViewModelManager()
    }

    fun <T : ActivityViewModel> fetch(context: Context, viewModelClass: Class<T>, savedInstanceState: Bundle?): T {
        val id = fetchId(savedInstanceState)
        var activityViewModel = viewModels.get(id)

        if (activityViewModel == null) {
            activityViewModel = create(context, viewModelClass, savedInstanceState, id)
        }

        return activityViewModel as T
    }

    fun destroy(activityViewModel: ActivityViewModel) {
        activityViewModel.onDestroy()

        viewModels.entries
                .filter { activityViewModel == it.value }
                .forEach { viewModels.remove(it.key) }
    }

    fun save(activityViewModel: ActivityViewModel, envelope: Bundle) {
        envelope.putString(VIEW_MODEL_ID_KEY, findIdForViewModel(activityViewModel))
        val state = Bundle()
        envelope.putBundle(VIEW_MODEL_STATE_KEY, state)
    }

    private fun <T : ActivityViewModel> create(context: Context, viewModelClass: Class<T>, savedInstanceState: Bundle?, id: String): T {
        val app: App = context.applicationContext as App
        val environment = app.component.environment()
        val activityViewModel: ActivityViewModel
        try {
            val constructor = viewModelClass.getConstructor(Environment::class.java)
            activityViewModel = constructor.newInstance(environment)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        viewModels.put(id, activityViewModel)
        activityViewModel.onCreate()
        return activityViewModel
    }

    private fun fetchId(savedInstanceState: Bundle?): String {
        if (savedInstanceState != null)
            return savedInstanceState.getString(VIEW_MODEL_ID_KEY)
        else
            return UUID.randomUUID().toString()
    }

    private fun findIdForViewModel(activityViewModel: ActivityViewModel): String {
        viewModels.entries
                .filter { activityViewModel == it.value }
                .forEach { return it.key }
        throw RuntimeException("Cannot find view model in map!")
    }
}