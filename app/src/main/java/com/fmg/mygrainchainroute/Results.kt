package com.fmg.mygrainchainroute

import android.util.Log
import java.lang.Exception

sealed class Results<out T: Any> {

    companion object{
        const val TAG = "BattalionException"
    }

    data class Success<out T: Any>(val data:T) : Results<T>()
    data class Error(val exception: Exception, var code:Int = 0) : Results<Nothing>(){
        init {
            Log.e(TAG, exception.message, exception)
        }
    }
}