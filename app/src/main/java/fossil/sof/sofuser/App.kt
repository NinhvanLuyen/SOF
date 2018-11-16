package fossil.sof.sofuser

import android.content.Context
import android.content.SharedPreferences
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import fossil.sof.sofuser.di.AppModules
import fossil.sof.sofuser.di.ApplicationComponent
import fossil.sof.sofuser.utils.AppUtils
import fossil.sof.sofuser.utils.ValidateUtils
import fossil.sof.sofuser.application.Configs
import fossil.sof.sofuser.di.DaggerApplicationComponent
import fossil.sof.sofuser.libs.utils.RxBus
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by ninhvanluyen on 1/29/18.
 */
class App : MultiDexApplication() {
    companion object {
        lateinit var app: App
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .appModules(AppModules(this))
                .build()

    }

    override fun onCreate() {
        super.onCreate()
        app = this
        component.inject(this)
        initializeTimber()
        Fresco.initialize(this)
    }

    private fun initializeTimber() {
        if (Configs.IS_DEBUG)
            Timber.plant(Timber.DebugTree())
    }



    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}