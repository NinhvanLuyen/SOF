package fossil.sof.sofuser.libs

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import fossil.sof.sofuser.domain.usecases.UserUseCase

/**
 * Created by ninhvanluyen on 16/11/18.
 */
data class Environment(val gson: Gson,
                       val resources: Resources,
                       val userUseCase: UserUseCase,
                       val context: Context)