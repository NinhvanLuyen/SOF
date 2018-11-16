package fossil.sof.sofuser.libs

import android.content.Context
import android.os.Bundle
import fossil.sof.sofuser.App
import java.util.*

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class FragmentViewModelManager {
    private val VIEW_MODEL_ID_KEY = "fragment_view_model_id"
    private val VIEW_MODEL_STATE_KEY = "fragment_view_model_state"
    private val viewModels = hashMapOf<String, FragmentViewModel>()

    companion object {
        val instance = FragmentViewModelManager()
    }

    fun <T : FragmentViewModel> fetch(context: Context, viewModelClass: Class<T>, savedInstanceState: Bundle?): T {
        val id = fetchId(savedInstanceState)
        var fragmentViewModel = viewModels.get(id)

        if (fragmentViewModel == null) {
            fragmentViewModel = create(context, viewModelClass, savedInstanceState, id)
        }

        return fragmentViewModel as T
    }

    fun destroy(fragmentViewModel: FragmentViewModel) {
        fragmentViewModel.onDestroy()

        viewModels.entries
                .filter { fragmentViewModel == it.value }
                .forEach { viewModels.remove(it.key) }
    }

    fun save(fragmentViewModel: FragmentViewModel, envelope: Bundle) {
        envelope.putString(VIEW_MODEL_ID_KEY, findIdForViewModel(fragmentViewModel))
        val state = Bundle()
        envelope.putBundle(VIEW_MODEL_STATE_KEY, state)
    }

    private fun <T : FragmentViewModel> create(context: Context, viewModelClass: Class<T>, savedInstanceState: Bundle?, id: String): T {
        val app: App = context.applicationContext as App
        val environment = app.component.environment()
        val fragmentViewModel: FragmentViewModel
        try {
            val constructor = viewModelClass.getConstructor(Environment::class.java)
            fragmentViewModel = constructor.newInstance(environment)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        viewModels.put(id, fragmentViewModel)
        fragmentViewModel.onCreate()
        return fragmentViewModel
    }

    private fun fetchId(savedInstanceState: Bundle?): String {
        if (savedInstanceState != null)
            return savedInstanceState.getString(VIEW_MODEL_ID_KEY)
        else
            return UUID.randomUUID().toString()
    }

    private fun findIdForViewModel(fragmentViewModel: FragmentViewModel): String {
        viewModels.entries
                .filter { fragmentViewModel == it.value }
                .forEach { return it.key }
        throw RuntimeException("Cannot find view model in map!")
    }

}