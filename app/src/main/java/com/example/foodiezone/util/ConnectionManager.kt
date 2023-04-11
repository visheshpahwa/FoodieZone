package com.example.foodiezone.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context: Context):Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo?=connectivityManager.activeNetworkInfo

        if(activeNetwork?.isConnected!=null){
            return activeNetwork.isConnected
        }
        else{
            return false
        }
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null
        //return (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting)
    }
}