package com.hpk.funnypet.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.*
import android.os.Build
import com.hpk.funnypet.AndroidApplication


object NetworkUtil {
    private const val TYPE_WIFI = 1
    private const val TYPE_MOBILE = 2
    private const val TYPE_NOT_CONNECTED = 0

    val isNetworkConnected: Boolean
    get() = getConnectivityStatus(AndroidApplication.mInstance.applicationContext) != TYPE_NOT_CONNECTED

    private fun getConnectivityStatus(context: Context): Int {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return TYPE_NOT_CONNECTED
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(network) ?: return TYPE_NOT_CONNECTED
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> TYPE_WIFI
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> TYPE_MOBILE
            else -> TYPE_NOT_CONNECTED
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun setOnNetworkDisconnectListener(context: Context, action: () -> Unit = {}) {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback: ConnectivityManager.NetworkCallback =
            object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)
                    action.invoke()
                }
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }
}