package fossil.sof.sofuser.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import fossil.sof.sofuser.App
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by ninhvanluyen on 16/11/18.
 */
//class ConnectionListener : BroadcastReceiver() {
//
//    @SuppressLint("MissingPermission")
//    @Inject
//    override fun onReceive(context: Context, intent: Intent) {
//        App.app.component.inject(this)
//        val connectivityManager = context
//                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetInfo = connectivityManager.activeNetworkInfo
//        if (activeNetInfo != null && activeNetInfo.isConnected) {
//            Timber.e("Turn on network")
//        } else {
//            Timber.e("Turn off network")
//        }
//    }
//}