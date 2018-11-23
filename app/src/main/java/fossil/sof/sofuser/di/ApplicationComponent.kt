package fossil.sof.sofuser.di

import fossil.sof.sofuser.libs.Environment
//import fossil.sof.sofuser.services.ConnectionListener
import dagger.Component
import fossil.sof.sofuser.App
import javax.inject.Singleton

/**
 * Created by ninhvanluyen on 16/11/18.
 */
@Singleton
@Component(modules = arrayOf(AppModules::class))
interface ApplicationComponent {
    fun environment(): Environment
    fun inject(app: App)
}